package com.moodsense.ai.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.moodsense.ai.data.local.entities.MoodJournalEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MoodJournalDao_Impl implements MoodJournalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MoodJournalEntity> __insertionAdapterOfMoodJournalEntity;

  private final EntityDeletionOrUpdateAdapter<MoodJournalEntity> __deletionAdapterOfMoodJournalEntity;

  private final EntityDeletionOrUpdateAdapter<MoodJournalEntity> __updateAdapterOfMoodJournalEntity;

  public MoodJournalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMoodJournalEntity = new EntityInsertionAdapter<MoodJournalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `mood_journal` (`id`,`note`,`tags`,`detectedEmotion`,`timestamp`,`mood`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MoodJournalEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getNote());
        statement.bindString(3, entity.getTags());
        if (entity.getDetectedEmotion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDetectedEmotion());
        }
        statement.bindLong(5, entity.getTimestamp());
        statement.bindLong(6, entity.getMood());
      }
    };
    this.__deletionAdapterOfMoodJournalEntity = new EntityDeletionOrUpdateAdapter<MoodJournalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `mood_journal` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MoodJournalEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMoodJournalEntity = new EntityDeletionOrUpdateAdapter<MoodJournalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `mood_journal` SET `id` = ?,`note` = ?,`tags` = ?,`detectedEmotion` = ?,`timestamp` = ?,`mood` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MoodJournalEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getNote());
        statement.bindString(3, entity.getTags());
        if (entity.getDetectedEmotion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDetectedEmotion());
        }
        statement.bindLong(5, entity.getTimestamp());
        statement.bindLong(6, entity.getMood());
        statement.bindLong(7, entity.getId());
      }
    };
  }

  @Override
  public Object insertEntry(final MoodJournalEntity entry,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMoodJournalEntity.insertAndReturnId(entry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteEntry(final MoodJournalEntity entry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMoodJournalEntity.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateEntry(final MoodJournalEntity entry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMoodJournalEntity.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MoodJournalEntity>> getAllEntries() {
    final String _sql = "SELECT * FROM mood_journal ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"mood_journal"}, new Callable<List<MoodJournalEntity>>() {
      @Override
      @NonNull
      public List<MoodJournalEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfDetectedEmotion = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedEmotion");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final List<MoodJournalEntity> _result = new ArrayList<MoodJournalEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MoodJournalEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpDetectedEmotion;
            if (_cursor.isNull(_cursorIndexOfDetectedEmotion)) {
              _tmpDetectedEmotion = null;
            } else {
              _tmpDetectedEmotion = _cursor.getString(_cursorIndexOfDetectedEmotion);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final int _tmpMood;
            _tmpMood = _cursor.getInt(_cursorIndexOfMood);
            _item = new MoodJournalEntity(_tmpId,_tmpNote,_tmpTags,_tmpDetectedEmotion,_tmpTimestamp,_tmpMood);
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
  public Flow<List<MoodJournalEntity>> getEntriesInRange(final long startTime, final long endTime) {
    final String _sql = "SELECT * FROM mood_journal WHERE timestamp >= ? AND timestamp <= ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"mood_journal"}, new Callable<List<MoodJournalEntity>>() {
      @Override
      @NonNull
      public List<MoodJournalEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfDetectedEmotion = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedEmotion");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final List<MoodJournalEntity> _result = new ArrayList<MoodJournalEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MoodJournalEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpDetectedEmotion;
            if (_cursor.isNull(_cursorIndexOfDetectedEmotion)) {
              _tmpDetectedEmotion = null;
            } else {
              _tmpDetectedEmotion = _cursor.getString(_cursorIndexOfDetectedEmotion);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final int _tmpMood;
            _tmpMood = _cursor.getInt(_cursorIndexOfMood);
            _item = new MoodJournalEntity(_tmpId,_tmpNote,_tmpTags,_tmpDetectedEmotion,_tmpTimestamp,_tmpMood);
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
  public Object getLatestEntry(final Continuation<? super MoodJournalEntity> $completion) {
    final String _sql = "SELECT * FROM mood_journal ORDER BY timestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MoodJournalEntity>() {
      @Override
      @Nullable
      public MoodJournalEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfDetectedEmotion = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedEmotion");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final MoodJournalEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpDetectedEmotion;
            if (_cursor.isNull(_cursorIndexOfDetectedEmotion)) {
              _tmpDetectedEmotion = null;
            } else {
              _tmpDetectedEmotion = _cursor.getString(_cursorIndexOfDetectedEmotion);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final int _tmpMood;
            _tmpMood = _cursor.getInt(_cursorIndexOfMood);
            _result = new MoodJournalEntity(_tmpId,_tmpNote,_tmpTags,_tmpDetectedEmotion,_tmpTimestamp,_tmpMood);
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
  public Object getEntryCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM mood_journal";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
