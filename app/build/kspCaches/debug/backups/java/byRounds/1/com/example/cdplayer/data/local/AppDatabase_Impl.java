package com.example.cdplayer.data.local;

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
import com.example.cdplayer.data.local.dao.AudioFileDao;
import com.example.cdplayer.data.local.dao.AudioFileDao_Impl;
import com.example.cdplayer.data.local.dao.BookmarkDao;
import com.example.cdplayer.data.local.dao.BookmarkDao_Impl;
import com.example.cdplayer.data.local.dao.PlaylistDao;
import com.example.cdplayer.data.local.dao.PlaylistDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AudioFileDao _audioFileDao;

  private volatile PlaylistDao _playlistDao;

  private volatile BookmarkDao _bookmarkDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `audio_files` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `artist` TEXT, `album` TEXT, `albumArtist` TEXT, `duration` INTEGER NOT NULL, `path` TEXT NOT NULL, `type` TEXT NOT NULL, `coverArtPath` TEXT, `coverArtUri` TEXT, `lastPlayedPosition` INTEGER NOT NULL, `lastPlayedAt` INTEGER, `addedDate` INTEGER NOT NULL, `genre` TEXT, `trackNumber` INTEGER, `discNumber` INTEGER, `year` INTEGER, `bitrate` INTEGER, `sampleRate` INTEGER, `fileSize` INTEGER, `mimeType` TEXT, `isFavorite` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_audio_files_path` ON `audio_files` (`path`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_audio_files_artist` ON `audio_files` (`artist`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_audio_files_album` ON `audio_files` (`album`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_audio_files_type` ON `audio_files` (`type`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_audio_files_lastPlayedAt` ON `audio_files` (`lastPlayedAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `playlists` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `createdDate` INTEGER NOT NULL, `modifiedDate` INTEGER NOT NULL, `coverArtPath` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `playlist_tracks` (`playlistId` INTEGER NOT NULL, `audioFileId` INTEGER NOT NULL, `orderIndex` INTEGER NOT NULL, `addedDate` INTEGER NOT NULL, PRIMARY KEY(`playlistId`, `audioFileId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bookmarks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `audioFileId` INTEGER NOT NULL, `position` INTEGER NOT NULL, `note` TEXT, `createdDate` INTEGER NOT NULL, FOREIGN KEY(`audioFileId`) REFERENCES `audio_files`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookmarks_audioFileId` ON `bookmarks` (`audioFileId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '1564f681777fd6212d9e6fed5654c185')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `audio_files`");
        db.execSQL("DROP TABLE IF EXISTS `playlists`");
        db.execSQL("DROP TABLE IF EXISTS `playlist_tracks`");
        db.execSQL("DROP TABLE IF EXISTS `bookmarks`");
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
        db.execSQL("PRAGMA foreign_keys = ON");
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
        final HashMap<String, TableInfo.Column> _columnsAudioFiles = new HashMap<String, TableInfo.Column>(22);
        _columnsAudioFiles.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("artist", new TableInfo.Column("artist", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("album", new TableInfo.Column("album", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("albumArtist", new TableInfo.Column("albumArtist", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("duration", new TableInfo.Column("duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("path", new TableInfo.Column("path", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("coverArtPath", new TableInfo.Column("coverArtPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("coverArtUri", new TableInfo.Column("coverArtUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("lastPlayedPosition", new TableInfo.Column("lastPlayedPosition", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("lastPlayedAt", new TableInfo.Column("lastPlayedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("addedDate", new TableInfo.Column("addedDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("genre", new TableInfo.Column("genre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("trackNumber", new TableInfo.Column("trackNumber", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("discNumber", new TableInfo.Column("discNumber", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("year", new TableInfo.Column("year", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("bitrate", new TableInfo.Column("bitrate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("sampleRate", new TableInfo.Column("sampleRate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("fileSize", new TableInfo.Column("fileSize", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("mimeType", new TableInfo.Column("mimeType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudioFiles.put("isFavorite", new TableInfo.Column("isFavorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAudioFiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAudioFiles = new HashSet<TableInfo.Index>(5);
        _indicesAudioFiles.add(new TableInfo.Index("index_audio_files_path", true, Arrays.asList("path"), Arrays.asList("ASC")));
        _indicesAudioFiles.add(new TableInfo.Index("index_audio_files_artist", false, Arrays.asList("artist"), Arrays.asList("ASC")));
        _indicesAudioFiles.add(new TableInfo.Index("index_audio_files_album", false, Arrays.asList("album"), Arrays.asList("ASC")));
        _indicesAudioFiles.add(new TableInfo.Index("index_audio_files_type", false, Arrays.asList("type"), Arrays.asList("ASC")));
        _indicesAudioFiles.add(new TableInfo.Index("index_audio_files_lastPlayedAt", false, Arrays.asList("lastPlayedAt"), Arrays.asList("ASC")));
        final TableInfo _infoAudioFiles = new TableInfo("audio_files", _columnsAudioFiles, _foreignKeysAudioFiles, _indicesAudioFiles);
        final TableInfo _existingAudioFiles = TableInfo.read(db, "audio_files");
        if (!_infoAudioFiles.equals(_existingAudioFiles)) {
          return new RoomOpenHelper.ValidationResult(false, "audio_files(com.example.cdplayer.data.local.entity.AudioFileEntity).\n"
                  + " Expected:\n" + _infoAudioFiles + "\n"
                  + " Found:\n" + _existingAudioFiles);
        }
        final HashMap<String, TableInfo.Column> _columnsPlaylists = new HashMap<String, TableInfo.Column>(6);
        _columnsPlaylists.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylists.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylists.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylists.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylists.put("modifiedDate", new TableInfo.Column("modifiedDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylists.put("coverArtPath", new TableInfo.Column("coverArtPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaylists = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlaylists = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlaylists = new TableInfo("playlists", _columnsPlaylists, _foreignKeysPlaylists, _indicesPlaylists);
        final TableInfo _existingPlaylists = TableInfo.read(db, "playlists");
        if (!_infoPlaylists.equals(_existingPlaylists)) {
          return new RoomOpenHelper.ValidationResult(false, "playlists(com.example.cdplayer.data.local.entity.PlaylistEntity).\n"
                  + " Expected:\n" + _infoPlaylists + "\n"
                  + " Found:\n" + _existingPlaylists);
        }
        final HashMap<String, TableInfo.Column> _columnsPlaylistTracks = new HashMap<String, TableInfo.Column>(4);
        _columnsPlaylistTracks.put("playlistId", new TableInfo.Column("playlistId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("audioFileId", new TableInfo.Column("audioFileId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("orderIndex", new TableInfo.Column("orderIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("addedDate", new TableInfo.Column("addedDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaylistTracks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlaylistTracks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlaylistTracks = new TableInfo("playlist_tracks", _columnsPlaylistTracks, _foreignKeysPlaylistTracks, _indicesPlaylistTracks);
        final TableInfo _existingPlaylistTracks = TableInfo.read(db, "playlist_tracks");
        if (!_infoPlaylistTracks.equals(_existingPlaylistTracks)) {
          return new RoomOpenHelper.ValidationResult(false, "playlist_tracks(com.example.cdplayer.data.local.entity.PlaylistTrackCrossRef).\n"
                  + " Expected:\n" + _infoPlaylistTracks + "\n"
                  + " Found:\n" + _existingPlaylistTracks);
        }
        final HashMap<String, TableInfo.Column> _columnsBookmarks = new HashMap<String, TableInfo.Column>(5);
        _columnsBookmarks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("audioFileId", new TableInfo.Column("audioFileId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("position", new TableInfo.Column("position", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBookmarks = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBookmarks.add(new TableInfo.ForeignKey("audio_files", "CASCADE", "NO ACTION", Arrays.asList("audioFileId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBookmarks = new HashSet<TableInfo.Index>(1);
        _indicesBookmarks.add(new TableInfo.Index("index_bookmarks_audioFileId", false, Arrays.asList("audioFileId"), Arrays.asList("ASC")));
        final TableInfo _infoBookmarks = new TableInfo("bookmarks", _columnsBookmarks, _foreignKeysBookmarks, _indicesBookmarks);
        final TableInfo _existingBookmarks = TableInfo.read(db, "bookmarks");
        if (!_infoBookmarks.equals(_existingBookmarks)) {
          return new RoomOpenHelper.ValidationResult(false, "bookmarks(com.example.cdplayer.data.local.entity.BookmarkEntity).\n"
                  + " Expected:\n" + _infoBookmarks + "\n"
                  + " Found:\n" + _existingBookmarks);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "1564f681777fd6212d9e6fed5654c185", "81cfc6dec9bfaf38df1a37c439837c70");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "audio_files","playlists","playlist_tracks","bookmarks");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `audio_files`");
      _db.execSQL("DELETE FROM `playlists`");
      _db.execSQL("DELETE FROM `playlist_tracks`");
      _db.execSQL("DELETE FROM `bookmarks`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
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
    _typeConvertersMap.put(AudioFileDao.class, AudioFileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlaylistDao.class, PlaylistDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BookmarkDao.class, BookmarkDao_Impl.getRequiredConverters());
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
  public AudioFileDao audioFileDao() {
    if (_audioFileDao != null) {
      return _audioFileDao;
    } else {
      synchronized(this) {
        if(_audioFileDao == null) {
          _audioFileDao = new AudioFileDao_Impl(this);
        }
        return _audioFileDao;
      }
    }
  }

  @Override
  public PlaylistDao playlistDao() {
    if (_playlistDao != null) {
      return _playlistDao;
    } else {
      synchronized(this) {
        if(_playlistDao == null) {
          _playlistDao = new PlaylistDao_Impl(this);
        }
        return _playlistDao;
      }
    }
  }

  @Override
  public BookmarkDao bookmarkDao() {
    if (_bookmarkDao != null) {
      return _bookmarkDao;
    } else {
      synchronized(this) {
        if(_bookmarkDao == null) {
          _bookmarkDao = new BookmarkDao_Impl(this);
        }
        return _bookmarkDao;
      }
    }
  }
}
