package com.inspiregeniussquad.handstogether.appStorage.daos;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.inspiregeniussquad.handstogether.appStorage.TeamData;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TeamDao_Impl implements TeamDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TeamData> __insertionAdapterOfTeamData;

  private final EntityInsertionAdapter<TeamData> __insertionAdapterOfTeamData_1;

  private final EntityDeletionOrUpdateAdapter<TeamData> __deletionAdapterOfTeamData;

  private final EntityDeletionOrUpdateAdapter<TeamData> __updateAdapterOfTeamData;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllTeams;

  public TeamDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTeamData = new EntityInsertionAdapter<TeamData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `TeamData` (`teamId`,`teamName`,`teamLogoUrl`,`teamMotto`,`teamMembersCount`) VALUES (nullif(?, 0),?,?,?,?)";
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
        return "INSERT OR REPLACE INTO `TeamData` (`teamId`,`teamName`,`teamLogoUrl`,`teamMotto`,`teamMembersCount`) VALUES (nullif(?, 0),?,?,?,?)";
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
  public void insert(final TeamData teamData) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTeamData.insert(teamData);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final ArrayList<TeamData> teamDataArrayList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTeamData_1.insert(teamDataArrayList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteTeam(final TeamData teamData) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTeamData.handle(teamData);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateTeam(final TeamData teamData) {
    __db.assertNotSuspendingTransaction();
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
    __db.assertNotSuspendingTransaction();
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
  public TeamData getTeamInfo(final String teamName) {
    final String _sql = "SELECT * from TeamData WHERE teamName = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (teamName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teamName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamId");
      final int _cursorIndexOfTeamName = CursorUtil.getColumnIndexOrThrow(_cursor, "teamName");
      final int _cursorIndexOfTeamLogoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "teamLogoUrl");
      final int _cursorIndexOfTeamMotto = CursorUtil.getColumnIndexOrThrow(_cursor, "teamMotto");
      final int _cursorIndexOfTeamMembersCount = CursorUtil.getColumnIndexOrThrow(_cursor, "teamMembersCount");
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
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamId");
      final int _cursorIndexOfTeamName = CursorUtil.getColumnIndexOrThrow(_cursor, "teamName");
      final int _cursorIndexOfTeamLogoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "teamLogoUrl");
      final int _cursorIndexOfTeamMotto = CursorUtil.getColumnIndexOrThrow(_cursor, "teamMotto");
      final int _cursorIndexOfTeamMembersCount = CursorUtil.getColumnIndexOrThrow(_cursor, "teamMembersCount");
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
