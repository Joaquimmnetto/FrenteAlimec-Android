package joaquimneto.com.alimec.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
* Created by KithLenovo on 23/01/2015.
*/
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DB_NAME = "ALIMEC_DB";
	private static final int DB_VERSION = 12;
//
	private static DatabaseHelper instance;

	public static void initiate(Context context) {
		instance = new DatabaseHelper(context);
	}

	public static DatabaseHelper getInstance() {
		return instance;
	}

	private DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, ProdutoDB.class);
			TableUtils.createTable(connectionSource, VendaDB.class);
			TableUtils.createTable(connectionSource, ItemDB.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
		try {
			TableUtils.dropTable(connectionSource, ProdutoDB.class, true);
			TableUtils.dropTable(connectionSource, ItemDB.class, true);
			TableUtils.dropTable(connectionSource, VendaDB.class, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		onCreate(sqLiteDatabase, connectionSource);
	}

}
