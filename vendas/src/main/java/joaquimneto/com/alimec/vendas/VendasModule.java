package joaquimneto.com.alimec.vendas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import joaquimneto.com.alimec.model.Item;
import joaquimneto.com.alimec.model.Produto;
import joaquimneto.com.alimec.model.Validador;
import joaquimneto.com.alimec.model.Venda;
import joaquimneto.com.alimec.persistence.AbstractDaoFactory;
import joaquimneto.com.alimec.persistence.DBModuleException;
import joaquimneto.com.alimec.persistence.IVendaDAO;
import joaquimneto.com.alimec.serverio.IServerModule;
import joaquimneto.com.alimec.serverio.ServerModuleException;

public class VendasModule implements IVendasModule {

    private IServerModule server;

    private Venda venda = new Venda();
    private List<Item> toRemoval = new ArrayList<Item>();

    private boolean enviado = false;
    private boolean salvo = false;
    private boolean finalizado = false;


    public VendasModule(IServerModule server) {
        this.server = server;
    }

    @Override
    public boolean carregar(long timestamp) throws VendasModuleException {
        Venda venda;
        try {
            venda = AbstractDaoFactory.getFactory().getVendaDAO().getVenda(timestamp);
            if (venda != null) {
                this.venda = venda;
                return true;
            }
        } catch (Exception e) {
            throw new VendasModuleException(e);
        }

        return false;
    }

    @Override
    public long criarItem(String codProduto, double quantidade, String unidade, double precoUnitario, MeioPgto meioPgto,
                          String comentario, String observacoes) throws VendasModuleException {
        try {
            double precoTotal = quantidade * precoUnitario;

            Produto produto = AbstractDaoFactory.getFactory().getProdutoRepository().getProduto(codProduto);
            if (produto == null) {
                return -1;
            }
            int itemId = venda.getItens().size();
            Item item = new Item(itemId, produto, venda, quantidade, unidade, precoUnitario, 0.0,
                    meioPgto.toString(), precoTotal, comentario, observacoes);


            Validador.validarItem(item);


            venda.getItens().add(item);

            return item.getId();

        } catch (Exception e) {
            throw new VendasModuleException(e);
        }
    }

    @Override
    public List<Item> getItens() {
        return Collections.unmodifiableList(venda.getItens());
    }

    @Override
    public boolean removerItem(long itemId) {
        if (itemId >= venda.getItens().size()) {
            return false;
        }

        toRemoval.add(venda.getItens().get((int) itemId));
        return true;
    }

    @Override
    public boolean atualizarItem(Item item) {
        for (int i = 0; i < venda.getItens().size(); i++) {
            Item it = venda.getItens().get(i);
            if (it.getId() == item.getId()) {
                it.setId(item.getId());
                it.setProduto(item.getProduto());
                it.setVenda(item.getVenda());
                it.setQuantidade(item.getQuantidade());
                it.setUnidade(item.getUnidade());
                it.setPrecoUnitario(item.getPrecoUnitario());
                it.setDescontos(item.getDescontos());
                it.setMeioPgto(item.getMeioPgto());
                it.setPrecoTotal(item.getPrecoTotal());
                it.setComentario(item.getComentario());
                it.setObservacoes(item.getObservacoes());
            }
        }

        return true;
    }

    @Override
    public VendaResult finalizar(String nomeCliente, String cpfCnpj) throws VendasModuleException {
        try {
            IVendaDAO dao = AbstractDaoFactory.getFactory().getVendaDAO();

            venda.setData(new Date());
            venda.setNomeCliente(nomeCliente);
            venda.setCpfCnpj(cpfCnpj);

            for (Item item : toRemoval) {
                venda.getItens().remove(item);
            }

            Validador.validarVenda(venda);

            try {
                if (!enviado) {
                    enviado = server.enviarVenda(venda);
                    venda.setEnviada(enviado);
                }
            } catch (ServerModuleException e) {
                e.printStackTrace();
                enviado = false;
            }

            try {
                if (!salvo) {
                    salvo = dao.addVenda(venda);
                }
            } catch (DBModuleException e) {
                e.printStackTrace();
                salvo = false;
            }
            if (enviado && salvo) {
                finalizado = true;
            }
            return new VendaResult(venda.getData().getTime(), salvo, enviado);
        } catch (Exception e) {
            throw new VendasModuleException(e);
        }
    }

    @Override
    public boolean isFinalizado() {
        return finalizado;
    }

    @Override
    public boolean enviarVendasPendentes() throws DBModuleException, ServerModuleException {

        boolean enviadoPendentes;

        IVendaDAO dao = AbstractDaoFactory.getFactory().getVendaDAO();
        Venda[] vendasPendentes = dao.getVendasPendentes();
        enviadoPendentes = server.enviarVendas(vendasPendentes);

        return enviadoPendentes;
    }


}
