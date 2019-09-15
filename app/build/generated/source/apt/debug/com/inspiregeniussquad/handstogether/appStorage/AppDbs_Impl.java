package com.inspiregeniussquad.handstogether.appStorage;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.inspiregeniussquad.handstogether.appStorage.daos.TeamDao;
import com.inspiregeniussquad.handstogether.appStorage.daos.TeamDao_Impl;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDbs_Impl extends AppDbs {
  private volatile TeamDao _teamDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `TeamData` (`teamId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `teamName` TEXT, `teamLogoUrl` TEXT, `teamMotto` TEXT, `teamMembersCount` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7af60c894866670a14b92db5eca54bc0')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `TeamData`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTeamData = new HashMap<String, TableInfo.Column>(5);
        _columnsTeamData.put("teamId", new TableInfo.Column("teamId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamData.put("teamName", new TableInfo.Column("teamName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamData.put("teamLogoUrl", new TableInfo.Column("teamLogoUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamData.put("teamMotto", new TableInfo.Column("teamMotto", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamData.put("teamMembersCount", new TableInfo.Column("teamMembersCount", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTeamData = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTeamData = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTeamData = new TableInfo("TeamData", _columnsTeamData, _foreignKeysTeamData, _indicesTeamData);
        final TableInfo _existingTeamData = TableInfo.read(_db, "TeamData");
        if (! _infoTeamData.equals(_existingTeamData)) {
          return new RoomOpenHelper.ValidationResult(false, "TeamData(com.inspiregeniussquad.handstogether.appStorage.TeamData).\n"
                  + " Expected:\n" + _infoTeamData + "\n"
                  + " Found:\n" + _existingTeamData);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "7af60c894866670a14b92db5eca54bc0", "d789e3691c5e373cbfd35683336dfc01");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "TeamData");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `TeamData`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public TeamDao teamDao() {
    if (_teamDao != null) {
      return _teamDao;
    } else {
      synchronized(this) {
        if(_teamDao == null) {
          _teamDao = new TeamDao_Impl(this);
        }
        return _teamDao;
      }
    }
  }
}
