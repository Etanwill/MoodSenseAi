package com.moodsense.ai.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.moodsense.ai.data.local.dao.EmotionHistoryDao;
import com.moodsense.ai.data.local.dao.EmotionHistoryDao_Impl;
import com.moodsense.ai.data.local.dao.MoodJournalDao;
import com.moodsense.ai.data.local.dao.MoodJournalDao_Impl;
import com.moodsense.ai.data.local.dao.SoundHistoryDao;
import com.moodsense.ai.data.local.dao.SoundHistoryDao_Impl;
import com.moodsense.ai.data.local.dao.TypingStatsDao;
import com.moodsense.ai.data.local.dao.TypingStatsDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MoodSenseDatabase_Impl extends MoodSenseDatabase {
  private volatile MoodJournalDao _moodJournalDao;

  private volatile TypingStatsDao _typingStatsDao;

  private volatile EmotionHistoryDao _emotionHistoryDao;

  private volatile SoundHistoryDao _soundHistoryDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `mood_journal` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `note` TEXT NOT NULL, `tags` TEXT NOT NULL, `detectedEmotion` TEXT, `timestamp` INTEGER NOT NULL, `mood` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `typing_stats` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `wordsPerMinute` REAL NOT NULL, `backspaceFrequency` REAL NOT NULL, `averagePauseDuration` INTEGER NOT NULL, `totalCharacters` INTEGER NOT NULL, `sessionDuration` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `emotion_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `emotion` TEXT NOT NULL, `confidence` REAL NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sound_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `category` TEXT NOT NULL, `confidence` REAL NOT NULL, `intensity` REAL NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5d9d2d1d6b584339b8b1627b77512ab4')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `mood_journal`");
        db.execSQL("DROP TABLE IF EXISTS `typing_stats`");
        db.execSQL("DROP TABLE IF EXISTS `emotion_history`");
        db.execSQL("DROP TABLE IF EXISTS `sound_history`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMoodJournal = new HashMap<String, TableInfo.Column>(6);
        _columnsMoodJournal.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodJournal.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodJournal.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodJournal.put("detectedEmotion", new TableInfo.Column("detectedEmotion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodJournal.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodJournal.put("mood", new TableInfo.Column("mood", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMoodJournal = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMoodJournal = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMoodJournal = new TableInfo("mood_journal", _columnsMoodJournal, _foreignKeysMoodJournal, _indicesMoodJournal);
        final TableInfo _existingMoodJournal = TableInfo.read(db, "mood_journal");
        if (!_infoMoodJournal.equals(_existingMoodJournal)) {
          return new RoomOpenHelper.ValidationResult(false, "mood_journal(com.moodsense.ai.data.local.entities.MoodJournalEntity).\n"
                  + " Expected:\n" + _infoMoodJournal + "\n"
                  + " Found:\n" + _existingMoodJournal);
        }
        final HashMap<String, TableInfo.Column> _columnsTypingStats = new HashMap<String, TableInfo.Column>(7);
        _columnsTypingStats.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTypingStats.put("wordsPerMinute", new TableInfo.Column("wordsPerMinute", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTypingStats.put("backspaceFrequency", new TableInfo.Column("backspaceFrequency", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTypingStats.put("averagePauseDuration", new TableInfo.Column("averagePauseDuration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTypingStats.put("totalCharacters", new TableInfo.Column("totalCharacters", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTypingStats.put("sessionDuration", new TableInfo.Column("sessionDuration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTypingStats.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTypingStats = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTypingStats = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTypingStats = new TableInfo("typing_stats", _columnsTypingStats, _foreignKeysTypingStats, _indicesTypingStats);
        final TableInfo _existingTypingStats = TableInfo.read(db, "typing_stats");
        if (!_infoTypingStats.equals(_existingTypingStats)) {
          return new RoomOpenHelper.ValidationResult(false, "typing_stats(com.moodsense.ai.data.local.entities.TypingStatsEntity).\n"
                  + " Expected:\n" + _infoTypingStats + "\n"
                  + " Found:\n" + _existingTypingStats);
        }
        final HashMap<String, TableInfo.Column> _columnsEmotionHistory = new HashMap<String, TableInfo.Column>(4);
        _columnsEmotionHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmotionHistory.put("emotion", new TableInfo.Column("emotion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmotionHistory.put("confidence", new TableInfo.Column("confidence", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmotionHistory.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEmotionHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEmotionHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEmotionHistory = new TableInfo("emotion_history", _columnsEmotionHistory, _foreignKeysEmotionHistory, _indicesEmotionHistory);
        final TableInfo _existingEmotionHistory = TableInfo.read(db, "emotion_history");
        if (!_infoEmotionHistory.equals(_existingEmotionHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "emotion_history(com.moodsense.ai.data.local.entities.EmotionHistoryEntity).\n"
                  + " Expected:\n" + _infoEmotionHistory + "\n"
                  + " Found:\n" + _existingEmotionHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsSoundHistory = new HashMap<String, TableInfo.Column>(5);
        _columnsSoundHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSoundHistory.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSoundHistory.put("confidence", new TableInfo.Column("confidence", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSoundHistory.put("intensity", new TableInfo.Column("intensity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSoundHistory.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSoundHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSoundHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSoundHistory = new TableInfo("sound_history", _columnsSoundHistory, _foreignKeysSoundHistory, _indicesSoundHistory);
        final TableInfo _existingSoundHistory = TableInfo.read(db, "sound_history");
        if (!_infoSoundHistory.equals(_existingSoundHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "sound_history(com.moodsense.ai.data.local.entities.SoundHistoryEntity).\n"
                  + " Expected:\n" + _infoSoundHistory + "\n"
                  + " Found:\n" + _existingSoundHistory);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "5d9d2d1d6b584339b8b1627b77512ab4", "fe56227b38ee570e79440e443e562110");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "mood_journal","typing_stats","emotion_history","sound_history");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `mood_journal`");
      _db.execSQL("DELETE FROM `typing_stats`");
      _db.execSQL("DELETE FROM `emotion_history`");
      _db.execSQL("DELETE FROM `sound_history`");
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
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(MoodJournalDao.class, MoodJournalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TypingStatsDao.class, TypingStatsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EmotionHistoryDao.class, EmotionHistoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SoundHistoryDao.class, SoundHistoryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public MoodJournalDao moodJournalDao() {
    if (_moodJournalDao != null) {
      return _moodJournalDao;
    } else {
      synchronized(this) {
        if(_moodJournalDao == null) {
          _moodJournalDao = new MoodJournalDao_Impl(this);
        }
        return _moodJournalDao;
      }
    }
  }

  @Override
  public TypingStatsDao typingStatsDao() {
    if (_typingStatsDao != null) {
      return _typingStatsDao;
    } else {
      synchronized(this) {
        if(_typingStatsDao == null) {
          _typingStatsDao = new TypingStatsDao_Impl(this);
        }
        return _typingStatsDao;
      }
    }
  }

  @Override
  public EmotionHistoryDao emotionHistoryDao() {
    if (_emotionHistoryDao != null) {
      return _emotionHistoryDao;
    } else {
      synchronized(this) {
        if(_emotionHistoryDao == null) {
          _emotionHistoryDao = new EmotionHistoryDao_Impl(this);
        }
        return _emotionHistoryDao;
      }
    }
  }

  @Override
  public SoundHistoryDao soundHistoryDao() {
    if (_soundHistoryDao != null) {
      return _soundHistoryDao;
    } else {
      synchronized(this) {
        if(_soundHistoryDao == null) {
          _soundHistoryDao = new SoundHistoryDao_Impl(this);
        }
        return _soundHistoryDao;
      }
    }
  }
}
