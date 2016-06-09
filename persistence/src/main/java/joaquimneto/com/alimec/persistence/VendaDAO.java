package joaquimneto.com.alimec.persistence;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import joaquimneto.com.alimec.model.Item;
import joaquimneto.com.alimec.model.Venda;

/**
 * Created by KithLenovo on 25/01/2015.
 */
class VendaDAO implements IVendaDAO {

    private Dao<VendaDB, Long> dao;

    VendaDAO(Dao<VendaDB, Long> dao) {
        this.dao = dao;
    }

    public boolean addVenda(Venda v) throws DBModuleException {
        IItemDAO itemDAO = AbstractDaoFactory.getFactory().getItemDAO();
        try {
            for (Item item : v.getItens()) {
                item.setVenda(v);
                itemDAO.addItem(item);
            }
            Dao.CreateOrUpdateStatus result = null;

            result = dao.createOrUpdate(new VendaDB(v));


            return result.isCreated() || result.isUpdated();
        } catch (Exception e) {
            throw new DBModuleException(e);
        }

    }

    @Override
    public Venda getVenda(long timestamp) throws DBModuleException {
        try {
            Venda venda = dao.queryForId(timestamp).toModelo();
            if (venda == null) {
                return null;
            }
            IItemDAO itemDAO = AbstractDaoFactory.getFactory().getItemDAO();

            venda.getItens().addAll(Arrays.asList(itemDAO.getItensFromVenda(venda)));

            return venda;

        } catch (Exception e) {
            throw new DBModuleException(e);
        }
    }

    public Venda[] getVendasAPartirDe(Date startingDate) throws DBModuleException {
        try {
            IItemDAO itemDAO = AbstractDaoFactory.getFactory().getItemDAO();
            List<Venda> vendas = new ArrayList<>();

            PreparedQuery<VendaDB> query = null;

            query = dao.queryBuilder().orderBy("data", true).where()
                    .gt("data", startingDate.getTime()).prepare();


            List<VendaDB> result = dao.query(query);

            for (VendaDB vendaDB : result) {
                Venda venda = vendaDB.toModelo();
                venda.getItens().addAll(Arrays.asList(itemDAO.getItensFromVenda(venda)));
                vendas.add(venda);
            }

            return vendas.toArray(new Venda[vendas.size()]);

        } catch (Exception e) {
            throw new DBModuleException(e);
        }
    }

    @Override
    public Venda[] getVendasPendentes() throws DBModuleException {
        try {
            IItemDAO itemDao = AbstractDaoFactory.getFactory().getItemDAO();
            List<Venda> vendas = new ArrayList<>();

            PreparedQuery<VendaDB> query = null;

            query = dao.queryBuilder().orderBy("data", true).where().eq("enviado", true).prepare();


            List<VendaDB> result = dao.query(query);

            for (VendaDB vendaDB : result) {
                Venda venda = vendaDB.toModelo();
                venda.getItens().addAll(Arrays.asList(itemDao.getItensFromVenda(venda)));
                vendas.add(venda);
            }

            return vendas.toArray(new Venda[vendas.size()]);
        } catch (SQLException e) {
            throw new DBModuleException(e);
        }
    }
}
