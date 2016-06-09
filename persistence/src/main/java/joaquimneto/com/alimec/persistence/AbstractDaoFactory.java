package joaquimneto.com.alimec.persistence;

public abstract class AbstractDaoFactory {

    private static DatabaseHelper helper;


	public static AbstractDaoFactory getFactory() {
		boolean android = System.getProperty("java.vm.name").equals("Dalvik");

		if (android) {
			return new AndroidDaoFactory(helper);
		}
		throw new ExceptionInInitializerError("No DAO available for this S.O.");
	}



	public abstract IItemDAO getItemDAO();

	public abstract IVendaDAO getVendaDAO();

	public abstract IProdutoRepository getProdutoRepository();
}
