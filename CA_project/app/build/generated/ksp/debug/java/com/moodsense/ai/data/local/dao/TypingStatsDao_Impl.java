package com.moodsense.ai.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.moodsense.ai.data.local.entities.TypingStatsEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TypingStatsDao_Impl implements TypingStatsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TypingStatsEntity> __insertionAdapterOfTypingStatsEntity;

  public TypingStatsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTypingStatsEntity = new EntityInsertionAdapter<TypingStatsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `typing_stats` (`id`,`wordsPerMinute`,`backspaceFrequency`,`averagePauseDuration`,`totalCharacters`,`sessionDuration`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TypingStatsEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getWordsPerMinute());
        statement.bindDouble(3, entity.getBackspaceFrequency());
        statement.bindLong(4, entity.getAveragePauseDuration());
        statement.bindLong(5, entity.getTotalCharacters());
        statement.bindLong(6, entity.getSessionDuration());
        statement.bindLong(7, entity.getTimestamp());
      }
    };
  }

  @Override
  public Object insertStats(final TypingStatsEntity stats,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTypingStatsEntity.insertAndReturnId(stats);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TypingStatsEntity>> getRecentStats() {
    final String _sql = "SELECT * FROM typing_stats ORDER BY timestamp DESC LIMIT 50";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"typing_stats"}, new Callable<List<TypingStatsEntity>>() {
      @Override
      @NonNull
      public List<TypingStatsEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWordsPerMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "wordsPerMinute");
          final int _cursorIndexOfBackspaceFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "backspaceFrequency");
          final int _cursorIndexOfAveragePauseDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "averagePauseDuration");
          final int _cursorIndexOfTotalCharacters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCharacters");
          final int _cursorIndexOfSessionDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionDuration");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<TypingStatsEntity> _result = new ArrayList<TypingStatsEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TypingStatsEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final float _tmpWordsPerMinute;
            _tmpWordsPerMinute = _cursor.getFloat(_cursorIndexOfWordsPerMinute);
            final float _tmpBackspaceFrequency;
            _tmpBackspaceFrequency = _cursor.getFloat(_cursorIndexOfBackspaceFrequency);
            final long _tmpAveragePauseDuration;
            _tmpAveragePauseDuration = _cursor.getLong(_cursorIndexOfAveragePauseDuration);
            final int _tmpTotalCharacters;
            _tmpTotalCharacters = _cursor.getInt(_cursorIndexOfTotalCharacters);
            final long _tmpSessionDuration;
            _tmpSessionDuration = _cursor.getLong(_cursorIndexOfSessionDuration);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new TypingStatsEntity(_tmpId,_tmpWordsPerMinute,_tmpBackspaceFrequency,_tmpAveragePauseDuration,_tmpTotalCharacters,_tmpSessionDuration,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAverageWpm(final long startTime, final Continuation<? super Float> $completion) {
    final String _sql = "SELECT AVG(wordsPerMinute) FROM typing_stats WHERE timestamp >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Float>() {
      @Override
      @Nullable
      public Float call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Float _result;
          if (_cursor.moveToFirst()) {
            final Float _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getFloat(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAverageBackspaceFrequency(final long startTime,
      final Continuation<? super Float> $completion) {
    final String _sql = "SELECT AVG(backspaceFrequency) FROM typing_stats WHERE timestamp >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Float>() {
      @Override
      @Nullable
      public Float call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Float _result;
          if (_cursor.moveToFirst()) {
            final Float _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getFloat(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TypingStatsEntity>> getAllStats() {
    final String _sql = "SELECT * FROM typing_stats ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"typing_stats"}, new Callable<List<TypingStatsEntity>>() {
      @Override
      @NonNull
      public List<TypingStatsEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWordsPerMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "wordsPerMinute");
          final int _cursorIndexOfBackspaceFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "backspaceFrequency");
          final int _cursorIndexOfAveragePauseDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "averagePauseDuration");
          final int _cursorIndexOfTotalCharacters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCharacters");
          final int _cursorIndexOfSessionDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionDuration");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<TypingStatsEntity> _result = new ArrayList<TypingStatsEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TypingStatsEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final float _tmpWordsPerMinute;
            _tmpWordsPerMinute = _cursor.getFloat(_cursorIndexOfWordsPerMinute);
            final float _tmpBackspaceFrequency;
            _tmpBackspaceFrequency = _cursor.getFloat(_cursorIndexOfBackspaceFrequency);
            final long _tmpAveragePauseDuration;
            _tmpAveragePauseDuration = _cursor.getLong(_cursorIndexOfAveragePauseDuration);
            final int _tmpTotalCharacters;
            _tmpTotalCharacters = _cursor.getInt(_cursorIndexOfTotalCharacters);
            final long _tmpSessionDuration;
            _tmpSessionDuration = _cursor.getLong(_cursorIndexOfSessionDuration);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new TypingStatsEntity(_tmpId,_tmpWordsPerMinute,_tmpBackspaceFrequency,_tmpAveragePauseDuration,_tmpTotalCharacters,_tmpSessionDuration,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
