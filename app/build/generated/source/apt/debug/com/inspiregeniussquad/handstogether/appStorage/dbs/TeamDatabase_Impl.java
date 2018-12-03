package com.inspiregeniussquad.handstogether.appStorage.dbs;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class TeamDatabase_Impl extends TeamDatabase {
  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `TeamData` (`teamId` INTEGER NOT NULL, `teamName` TEXT, `teamLogoUrl` TEXT, `teamMotto` TEXT, `teamMembersCount` TEXT, PRIMARY KEY(`teamId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"da0a962eefc29072a5368dc1b07d5c44\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `TeamData`");
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
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTeamData = new HashMap<String, TableInfo.Column>(5);
        _columnsTeamData.put("teamId", new TableInfo.Column("teamId", "INTEGER", true, 1));
        _columnsTeamData.put("teamName", new TableInfo.Column("teamName", "TEXT", false, 0));
        _columnsTeamData.put("teamLogoUrl", new TableInfo.Column("teamLogoUrl", "TEXT", false, 0));
        _columnsTeamData.put("teamMotto", new TableInfo.Column("teamMotto", "TEXT", false, 0));
        _columnsTeamData.put("teamMembersCount", new TableInfo.Column("teamMembersCount", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTeamData = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTeamData = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTeamData = new TableInfo("TeamData", _columnsTeamData, _foreignKeysTeamData, _indicesTeamData);
        final TableInfo _existingTeamData = TableInfo.read(_db, "TeamData");
        if (! _infoTeamData.equals(_existingTeamData)) {
          throw new IllegalStateException("Migration didn't properly handle TeamData(com.inspiregeniussquad.handstogether.appStorage.TeamData).\n"
                  + " Expected:\n" + _infoTeamData + "\n"
                  + " Found:\n" + _existingTeamData);
        }
      }
    }, "da0a962eefc29072a5368dc1b07d5c44", "f85e70ac266d5e57980372dfe2ccae57");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "TeamData");
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
}
