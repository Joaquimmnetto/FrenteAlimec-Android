package joaquimneto.com.alimec.persistence.dao;


import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import joaquimneto.com.alimec.model.Categoria;
import joaquimneto.com.alimec.model.Produto;
import joaquimneto.com.alimec.model.ProdutoComposite;
import joaquimneto.com.alimec.model.ProdutoTO;
import joaquimneto.com.alimec.persistence.ProdutoDB;
import joaquimneto.com.alimec.persistence.interfaces.IProdutoRepository;

public class ProdutoRepository implements IProdutoRepository {

    public static final String RAIZ_COD = "raiz";
    private Map<String, ProdutoComposite> produtoCompositeMap;
    private Produto[] produtos;
    private Dao<ProdutoDB, String> dao;

	ProdutoRepository(Dao<ProdutoDB, String> dao) {
		this.dao = dao;
	}

    public void addProdutos(ProdutoTO[] produtosTO) {
        try {
            Map<String, ProdutoTO> produtoTOMap = criarProdutoTOMap(produtosTO);
            Map<String, ProdutoDB> arvoreProdutoDB = criarArvoreProdutoDB(produtoTOMap);
            salvarArvoreProdutoDB(arvoreProdutoDB);
            produtoCompositeMap = criarProdutosFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public Produto[] getProdutos() {
        if (produtoCompositeMap == null) {
            produtoCompositeMap = criarProdutosFromDB();
        }
        if (produtos == null) {
            List<Produto> folhas = new ArrayList<>();
            for (ProdutoComposite produto : produtoCompositeMap.values()) {
                if (produto instanceof Produto) {
                    folhas.add((Produto) produto);
                }
            }

            produtos = folhas.toArray(new Produto[folhas.size()]);

            Arrays.sort(produtos, new Comparator<Produto>() {
                @Override
                public int compare(Produto lhs, Produto rhs) {
                    if (lhs == null && rhs == null) {
                        return 0;
                    }
                    if (lhs == null) {
                        return -1;
                    }
                    if (rhs == null) {
                        return 1;
                    }
                    return lhs.getCodigo().compareTo(rhs.getCodigo());
                }
            });
        }
        return produtos;

    }

    public String[] getListaCodigos() {
        return produtoCompositeMap.keySet().toArray(new String[produtoCompositeMap.keySet().size()]);
    }

    public ProdutoComposite getProdutoComposite(String codigo) {
        if(produtoCompositeMap == null){
            getProdutos();
        }
        if (produtoCompositeMap.containsKey(codigo)) {
            return produtoCompositeMap.get(codigo);
        } else {
            return null;
        }
    }

    public Produto getProduto(String prodCode) {

        ProdutoComposite prod = getProdutoComposite(prodCode);

        if (prod instanceof Produto) {
            return (Produto) prod;
        }

        return null;

    }

    public ProdutoDB getProdutoDB(ProdutoComposite produto) {
	    try {
	        return dao.queryForId(produto.getCodigo());
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	public void limparProdutos() {
    	try {
			dao.deleteBuilder().delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    private Map<String, ProdutoDB> getProdutosFromDatabase() throws SQLException {
        List<ProdutoDB> result = dao.queryForAll();
        Map<String, ProdutoDB> mapResult = new HashMap<>();

        for (ProdutoDB produto : result) {
            mapResult.put(produto.codigo, produto);
        }


        return mapResult;
    }

    private Map<String, ProdutoTO> criarProdutoTOMap(ProdutoTO[] produtos) {
        Map<String, ProdutoTO> produtosMap = new HashMap<>();
        for (ProdutoTO produto : produtos) {
            produtosMap.put(produto.codigo, produto);
        }

        return produtosMap;
    }

//    private Map<String, ProdutoDB> criarArvoreProdutoDB(Map<String, ProdutoComposite> produtoCompositeMap) {
//        Map<String, ProdutoDB> produtosDB = new HashMap<>();
//
//        for (ProdutoComposite produto : produtoCompositeMap.values()) {
//            ProdutoDB db = null;
//
//            if (produtosDB.containsKey(produto.getCodigo())) {
//                db = produtosDB.get(produto.getCodigo());
//            } else {
//                db = ProdutoDB.createProdutoDB(produto);
//            }
//
//            ProdutoComposite prodPai = ((Categoria) produto).getParente();
//
//            if (prodPai != null) {
//                ProdutoDB pai = null;
//                String codigoPai = prodPai.getCodigo();
//                if (produtosDB.containsKey(codigoPai)) {
//                    pai = produtosDB.get(codigoPai);
//                } else {
//                    pai = ProdutoDB.createProdutoDB(produto);
//                }
//                db.parente = pai;
//            } else {
//                db.parente = null;
//            }
//        }
//
//        return produtosDB;
//    }

    private Map<String, ProdutoDB> criarArvoreProdutoDB(Map<String, ProdutoTO> produtos) {
        Map<String, ProdutoDB> produtosDB = new HashMap<>();

        for (ProdutoTO produto : produtos.values()) {
            ProdutoDB produtoDB = null;

            if (produtosDB.containsKey(produto.codigo)) {
                produtoDB = produtosDB.get(produto.codigo);
            } else {
                produtoDB = new ProdutoDB(produto);
            }

            String codigoPai = produto.parenteCod;

            if (codigoPai != null) {
                ProdutoDB pai = produtosDB.get(codigoPai);

                if (!produtosDB.containsKey(codigoPai)) {
                    if (produtos.get(codigoPai) != null) {
                        pai = new ProdutoDB(produtos.get(codigoPai));
                        produtosDB.put(pai.codigo, pai);

                    } else {
                        produtoDB.parente = null;
                    }
                }
                produtoDB.parente = pai;
            } else {
                produtoDB.parente = null;
            }

            produtosDB.put(produtoDB.codigo, produtoDB);
        }

        return produtosDB;
    }

//    private Map<String, ProdutoDB> criarESalvarArvoreProdutosDB(ProdutoTO[] produtos) throws SQLException {
//        Map<String, ProdutoDB> produtosDB = criarArvoreProdutoDB(criarProdutoTOMap(produtos));
//        salvarArvoreProdutoDB(produtosDB);
//
//        return produtosDB;
//    }

    //    salvarArvoreProdutoDB(criarArvoreProdutoDB(criarProdutoMap(produtosArray)));
    private void salvarArvoreProdutoDB(final Map<String, ProdutoDB> produtosDB) throws SQLException {
        try {
            dao.callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (ProdutoDB produto : produtosDB.values()) {
                        if (!produto.categoria) {
                            salvarCategoria(produto.parente);
                            dao.createOrUpdate(produto);
                        }
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    private void salvarCategoria(ProdutoDB categoria) throws SQLException {
        if (categoria != null && !dao.idExists(categoria.codigo)) {
            salvarCategoria(categoria.parente);
            dao.createOrUpdate(categoria);
        }
    }

    private Map<String, ProdutoComposite> criarProdutosFromDB() {
        try {
            return criarProdutosFromDB(getProdutosFromDatabase());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private Map<String, ProdutoComposite> criarProdutosFromDB(Map<String, ProdutoDB> produtosDB) {
        Map<String, ProdutoComposite> produtos = new HashMap<>();
        ProdutoDB raizDB = produtosDB.get(RAIZ_COD);
        if (raizDB != null) {
            Categoria raiz = (Categoria) criarArvoreTopDown(raizDB, produtosDB);
            raiz.setParente(null);
            preencherBottomUp(raiz);
            produtos = criarMapaFromArvore(raiz);

        }
        return produtos;
    }

    private Map<String, ProdutoComposite> criarMapaFromArvore(ProdutoComposite raiz) {
        Map<String, ProdutoComposite> produtos = new HashMap<>();
        if (raiz instanceof Categoria) {
            for (ProdutoComposite filho : ((Categoria) raiz).getFilhos()) {
                produtos.putAll(criarMapaFromArvore(filho));
            }
        }
        produtos.put(raiz.getCodigo(), raiz);

        return produtos;
    }

    private void preencherBottomUp(Categoria raiz) {
        for (ProdutoComposite filho : raiz.getFilhos()) {
            filho.setParente(raiz);
            if (filho instanceof Categoria) {
                preencherBottomUp((Categoria) filho);
            }
        }
    }

    private ProdutoComposite criarArvoreTopDown(ProdutoDB pai, Map<String, ProdutoDB> produtosDB) {

        if (pai.categoria) {
            List<ProdutoDB> filhosDB = new ArrayList<>();
            for (ProdutoDB filhoCandidato : produtosDB.values()) {
                if (filhoCandidato.parente != null && filhoCandidato.parente.equals(pai)) {
                    filhosDB.add(filhoCandidato);
                }
            }

            List<ProdutoComposite> filhos = new ArrayList<>();
            for (ProdutoDB filho : filhosDB) {
                filhos.add(criarArvoreTopDown(filho, produtosDB));
            }

            return new Categoria(pai.codigo, pai.descricao, filhos.toArray(new ProdutoComposite[filhos.size()]));
        }

        return new Produto(pai.codigo, pai.descricao);

    }


//
//    private Produto parseProduto(Cursor c){
//        String codigo = c.getString(0);
//        String descricao = c.getString(1);
//
//        return new Produto(codigo,descricao);
//    }


}
