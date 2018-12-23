package com.inspiregeniussquad.handstogether.appStorage.daos;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import com.inspiregeniussquad.handstogether.appStorage.TeamData;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class TeamDao_Impl implements TeamDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTeamData;

  private final EntityInsertionAdapter __insertionAdapterOfTeamData_1;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfTeamData;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfTeamData;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllTeams;

  public TeamDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTeamData = new EntityInsertionAdapter<TeamData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `TeamData`(`teamId`,`teamName`,`teamLogoUrl`,`teamMotto`,`teamMembersCount`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TeamData value) {
        stmt.bindLong(1, value.getTeamId());
        if (value.getTeamName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTeamName());
        }
        if (value.getTeamLogoUrl() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTeamLogoUrl());
        }
        if (value.getTeamMotto() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTeamMotto());
        }
        if (value.getTeamMembersCount() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getTeamMembersCount());
        }
      }
    };
    this.__insertionAdapterOfTeamData_1 = new EntityInsertionAdapter<TeamData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `TeamData`(`teamId`,`teamName`,`teamLogoUrl`,`teamMotto`,`teamMembersCount`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TeamData value) {
        stmt.bindLong(1, value.getTeamId());
        if (value.getTeamName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTeamName());
        }
        if (value.getTeamLogoUrl() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTeamLogoUrl());
        }
        if (value.getTeamMotto() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTeamMotto());
        }
        if (value.getTeamMembersCount() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getTeamMembersCount());
        }
      }
    };
    this.__deletionAdapterOfTeamData = new EntityDeletionOrUpdateAdapter<TeamData>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `TeamData` WHERE `teamId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TeamData value) {
        stmt.bindLong(1, value.getTeamId());
      }
    };
    this.__updateAdapterOfTeamData = new EntityDeletionOrUpdateAdapter<TeamData>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `TeamData` SET `teamId` = ?,`teamName` = ?,`teamLogoUrl` = ?,`teamMotto` = ?,`teamMembersCount` = ? WHERE `teamId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TeamData value) {
        stmt.bindLong(1, value.getTeamId());
        if (value.getTeamName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTeamName());
        }
        if (value.getTeamLogoUrl() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTeamLogoUrl());
        }
        if (value.getTeamMotto() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTeamMotto());
        }
        if (value.getTeamMembersCount() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getTeamMembersCount());
        }
        stmt.bindLong(6, value.getTeamId());
      }
    };
    this.__preparedStmtOfDeleteAllTeams = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE from TeamData";
        return _query;
      }
    };
  }

  @Override
  public void insert(TeamData teamData) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfTeamData.insert(teamData);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(ArrayList<TeamData> teamDataArrayList) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfTeamData_1.insert(teamDataArrayList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteTeam(TeamData teamData) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfTeamData.handle(teamData);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateTeam(TeamData teamData) {
    __db.beginTransaction();
    try {
      __updateAdapterOfTeamData.handle(teamData);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllTeams() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllTeams.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllTeams.release(_stmt);
    }
  }

  @Override
  public TeamData getTeamInfo(String teamName) {
    final String _sql = "SELECT * from TeamData WHERE teamName = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (teamName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teamName);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfTeamId = _cursor.getColumnIndexOrThrow("teamId");
      final int _cursorIndexOfTeamName = _cursor.getColumnIndexOrThrow("teamName");
      final int _cursorIndexOfTeamLogoUrl = _cursor.getColumnIndexOrThrow("teamLogoUrl");
      final int _cursorIndexOfTeamMotto = _cursor.getColumnIndexOrThrow("teamMotto");
      final int _cursorIndexOfTeamMembersCount = _cursor.getColumnIndexOrThrow("teamMembersCount");
      final TeamData _result;
      if(_cursor.moveToFirst()) {
        _result = new TeamData();
        final int _tmpTeamId;
        _tmpTeamId = _cursor.getInt(_cursorIndexOfTeamId);
        _result.setTeamId(_tmpTeamId);
        final String _tmpTeamName;
        _tmpTeamName = _cursor.getString(_cursorIndexOfTeamName);
        _result.setTeamName(_tmpTeamName);
        final String _tmpTeamLogoUrl;
        _tmpTeamLogoUrl = _cursor.getString(_cursorIndexOfTeamLogoUrl);
        _result.setTeamLogoUrl(_tmpTeamLogoUrl);
        final String _tmpTeamMotto;
        _tmpTeamMotto = _cursor.getString(_cursorIndexOfTeamMotto);
        _result.setTeamMotto(_tmpTeamMotto);
        final String _tmpTeamMembersCount;
        _tmpTeamMembersCount = _cursor.getString(_cursorIndexOfTeamMembersCount);
        _result.setTeamMembersCount(_tmpTeamMembersCount);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public TeamData[] loadAll() {
    final String _sql = "Select * FROM TeamData";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfTeamId = _cursor.getColumnIndexOrThrow("teamId");
      final int _cursorIndexOfTeamName = _cursor.getColumnIndexOrThrow("teamName");
      final int _cursorIndexOfTeamLogoUrl = _cursor.getColumnIndexOrThrow("teamLogoUrl");
      final int _cursorIndexOfTeamMotto = _cursor.getColumnIndexOrThrow("teamMotto");
      final int _cursorIndexOfTeamMembersCount = _cursor.getColumnIndexOrThrow("teamMembersCount");
      final TeamData[] _result = new TeamData[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final TeamData _item;
        _item = new TeamData();
        final int _tmpTeamId;
        _tmpTeamId = _cursor.getInt(_cursorIndexOfTeamId);
        _item.setTeamId(_tmpTeamId);
        final String _tmpTeamName;
        _tmpTeamName = _cursor.getString(_cursorIndexOfTeamName);
        _item.setTeamName(_tmpTeamName);
        final String _tmpTeamLogoUrl;
        _tmpTeamLogoUrl = _cursor.getString(_cursorIndexOfTeamLogoUrl);
        _item.setTeamLogoUrl(_tmpTeamLogoUrl);
        final String _tmpTeamMotto;
        _tmpTeamMotto = _cursor.getString(_cursorIndexOfTeamMotto);
        _item.setTeamMotto(_tmpTeamMotto);
        final String _tmpTeamMembersCount;
        _tmpTeamMembersCount = _cursor.getString(_cursorIndexOfTeamMembersCount);
        _item.setTeamMembersCount(_tmpTeamMembersCount);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
