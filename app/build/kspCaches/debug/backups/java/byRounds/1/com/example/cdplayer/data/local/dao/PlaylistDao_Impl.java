package com.example.cdplayer.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.cdplayer.data.local.entity.AudioFileEntity;
import com.example.cdplayer.data.local.entity.PlaylistEntity;
import com.example.cdplayer.data.local.entity.PlaylistTrackCrossRef;
import java.lang.Boolean;
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
public final class PlaylistDao_Impl implements PlaylistDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlaylistEntity> __insertionAdapterOfPlaylistEntity;

  private final EntityInsertionAdapter<PlaylistTrackCrossRef> __insertionAdapterOfPlaylistTrackCrossRef;

  private final EntityDeletionOrUpdateAdapter<PlaylistEntity> __deletionAdapterOfPlaylistEntity;

  private final EntityDeletionOrUpdateAdapter<PlaylistEntity> __updateAdapterOfPlaylistEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfRemoveTrackFromPlaylist;

  private final SharedSQLiteStatement __preparedStmtOfClearPlaylist;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTrackOrder;

  public PlaylistDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlaylistEntity = new EntityInsertionAdapter<PlaylistEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `playlists` (`id`,`name`,`description`,`createdDate`,`modifiedDate`,`coverArtPath`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlaylistEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindLong(4, entity.getCreatedDate());
        statement.bindLong(5, entity.getModifiedDate());
        if (entity.getCoverArtPath() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCoverArtPath());
        }
      }
    };
    this.__insertionAdapterOfPlaylistTrackCrossRef = new EntityInsertionAdapter<PlaylistTrackCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `playlist_tracks` (`playlistId`,`audioFileId`,`orderIndex`,`addedDate`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlaylistTrackCrossRef entity) {
        statement.bindLong(1, entity.getPlaylistId());
        statement.bindLong(2, entity.getAudioFileId());
        statement.bindLong(3, entity.getOrderIndex());
        statement.bindLong(4, entity.getAddedDate());
      }
    };
    this.__deletionAdapterOfPlaylistEntity = new EntityDeletionOrUpdateAdapter<PlaylistEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `playlists` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlaylistEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPlaylistEntity = new EntityDeletionOrUpdateAdapter<PlaylistEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `playlists` SET `id` = ?,`name` = ?,`description` = ?,`createdDate` = ?,`modifiedDate` = ?,`coverArtPath` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlaylistEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindLong(4, entity.getCreatedDate());
        statement.bindLong(5, entity.getModifiedDate());
        if (entity.getCoverArtPath() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCoverArtPath());
        }
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM playlists WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveTrackFromPlaylist = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM playlist_tracks WHERE playlistId = ? AND audioFileId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearPlaylist = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM playlist_tracks WHERE playlistId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTrackOrder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE playlist_tracks\n"
                + "        SET orderIndex = ?\n"
                + "        WHERE playlistId = ? AND audioFileId = ?\n"
                + "    ";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final PlaylistEntity playlist,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPlaylistEntity.insertAndReturnId(playlist);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object addTrackToPlaylist(final PlaylistTrackCrossRef crossRef,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlaylistTrackCrossRef.insert(crossRef);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object addTracksToPlaylist(final List<PlaylistTrackCrossRef> crossRefs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlaylistTrackCrossRef.insert(crossRefs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final PlaylistEntity playlist,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPlaylistEntity.handle(playlist);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final PlaylistEntity playlist,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPlaylistEntity.handle(playlist);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object removeTrackFromPlaylist(final long playlistId, final long audioFileId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveTrackFromPlaylist.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, playlistId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, audioFileId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfRemoveTrackFromPlaylist.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearPlaylist(final long playlistId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearPlaylist.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, playlistId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearPlaylist.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTrackOrder(final long playlistId, final long audioFileId, final int newIndex,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTrackOrder.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, newIndex);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, playlistId);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, audioFileId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateTrackOrder.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PlaylistEntity>> getAllPlaylists() {
    final String _sql = "SELECT * FROM playlists ORDER BY modifiedDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"playlists"}, new Callable<List<PlaylistEntity>>() {
      @Override
      @NonNull
      public List<PlaylistEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final int _cursorIndexOfModifiedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "modifiedDate");
          final int _cursorIndexOfCoverArtPath = CursorUtil.getColumnIndexOrThrow(_cursor, "coverArtPath");
          final List<PlaylistEntity> _result = new ArrayList<PlaylistEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlaylistEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpCreatedDate;
            _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
            final long _tmpModifiedDate;
            _tmpModifiedDate = _cursor.getLong(_cursorIndexOfModifiedDate);
            final String _tmpCoverArtPath;
            if (_cursor.isNull(_cursorIndexOfCoverArtPath)) {
              _tmpCoverArtPath = null;
            } else {
              _tmpCoverArtPath = _cursor.getString(_cursorIndexOfCoverArtPath);
            }
            _item = new PlaylistEntity(_tmpId,_tmpName,_tmpDescription,_tmpCreatedDate,_tmpModifiedDate,_tmpCoverArtPath);
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
  public Object getPlaylistById(final long id,
      final Continuation<? super PlaylistEntity> $completion) {
    final String _sql = "SELECT * FROM playlists WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PlaylistEntity>() {
      @Override
      @Nullable
      public PlaylistEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final int _cursorIndexOfModifiedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "modifiedDate");
          final int _cursorIndexOfCoverArtPath = CursorUtil.getColumnIndexOrThrow(_cursor, "coverArtPath");
          final PlaylistEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpCreatedDate;
            _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
            final long _tmpModifiedDate;
            _tmpModifiedDate = _cursor.getLong(_cursorIndexOfModifiedDate);
            final String _tmpCoverArtPath;
            if (_cursor.isNull(_cursorIndexOfCoverArtPath)) {
              _tmpCoverArtPath = null;
            } else {
              _tmpCoverArtPath = _cursor.getString(_cursorIndexOfCoverArtPath);
            }
            _result = new PlaylistEntity(_tmpId,_tmpName,_tmpDescription,_tmpCreatedDate,_tmpModifiedDate,_tmpCoverArtPath);
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
  public Flow<PlaylistEntity> getPlaylistByIdFlow(final long id) {
    final String _sql = "SELECT * FROM playlists WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"playlists"}, new Callable<PlaylistEntity>() {
      @Override
      @Nullable
      public PlaylistEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final int _cursorIndexOfModifiedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "modifiedDate");
          final int _cursorIndexOfCoverArtPath = CursorUtil.getColumnIndexOrThrow(_cursor, "coverArtPath");
          final PlaylistEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpCreatedDate;
            _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
            final long _tmpModifiedDate;
            _tmpModifiedDate = _cursor.getLong(_cursorIndexOfModifiedDate);
            final String _tmpCoverArtPath;
            if (_cursor.isNull(_cursorIndexOfCoverArtPath)) {
              _tmpCoverArtPath = null;
            } else {
              _tmpCoverArtPath = _cursor.getString(_cursorIndexOfCoverArtPath);
            }
            _result = new PlaylistEntity(_tmpId,_tmpName,_tmpDescription,_tmpCreatedDate,_tmpModifiedDate,_tmpCoverArtPath);
          } else {
            _result = null;
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
  public Flow<List<AudioFileEntity>> getTracksForPlaylist(final long playlistId) {
    final String _sql = "\n"
            + "        SELECT af.* FROM audio_files af\n"
            + "        INNER JOIN playlist_tracks pt ON af.id = pt.audioFileId\n"
            + "        WHERE pt.playlistId = ?\n"
            + "        ORDER BY pt.orderIndex ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files",
        "playlist_tracks"}, new Callable<List<AudioFileEntity>>() {
      @Override
      @NonNull
      public List<AudioFileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfAlbum = CursorUtil.getColumnIndexOrThrow(_cursor, "album");
          final int _cursorIndexOfAlbumArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "albumArtist");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfPath = CursorUtil.getColumnIndexOrThrow(_cursor, "path");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCoverArtPath = CursorUtil.getColumnIndexOrThrow(_cursor, "coverArtPath");
          final int _cursorIndexOfCoverArtUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverArtUri");
          final int _cursorIndexOfLastPlayedPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedPosition");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfTrackNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "trackNumber");
          final int _cursorIndexOfDiscNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "discNumber");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfBitrate = CursorUtil.getColumnIndexOrThrow(_cursor, "bitrate");
          final int _cursorIndexOfSampleRate = CursorUtil.getColumnIndexOrThrow(_cursor, "sampleRate");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final List<AudioFileEntity> _result = new ArrayList<AudioFileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AudioFileEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpAlbum;
            if (_cursor.isNull(_cursorIndexOfAlbum)) {
              _tmpAlbum = null;
            } else {
              _tmpAlbum = _cursor.getString(_cursorIndexOfAlbum);
            }
            final String _tmpAlbumArtist;
            if (_cursor.isNull(_cursorIndexOfAlbumArtist)) {
              _tmpAlbumArtist = null;
            } else {
              _tmpAlbumArtist = _cursor.getString(_cursorIndexOfAlbumArtist);
            }
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final String _tmpPath;
            _tmpPath = _cursor.getString(_cursorIndexOfPath);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCoverArtPath;
            if (_cursor.isNull(_cursorIndexOfCoverArtPath)) {
              _tmpCoverArtPath = null;
            } else {
              _tmpCoverArtPath = _cursor.getString(_cursorIndexOfCoverArtPath);
            }
            final String _tmpCoverArtUri;
            if (_cursor.isNull(_cursorIndexOfCoverArtUri)) {
              _tmpCoverArtUri = null;
            } else {
              _tmpCoverArtUri = _cursor.getString(_cursorIndexOfCoverArtUri);
            }
            final long _tmpLastPlayedPosition;
            _tmpLastPlayedPosition = _cursor.getLong(_cursorIndexOfLastPlayedPosition);
            final Long _tmpLastPlayedAt;
            if (_cursor.isNull(_cursorIndexOfLastPlayedAt)) {
              _tmpLastPlayedAt = null;
            } else {
              _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            }
            final long _tmpAddedDate;
            _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            final String _tmpGenre;
            if (_cursor.isNull(_cursorIndexOfGenre)) {
              _tmpGenre = null;
            } else {
              _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
            }
            final Integer _tmpTrackNumber;
            if (_cursor.isNull(_cursorIndexOfTrackNumber)) {
              _tmpTrackNumber = null;
            } else {
              _tmpTrackNumber = _cursor.getInt(_cursorIndexOfTrackNumber);
            }
            final Integer _tmpDiscNumber;
            if (_cursor.isNull(_cursorIndexOfDiscNumber)) {
              _tmpDiscNumber = null;
            } else {
              _tmpDiscNumber = _cursor.getInt(_cursorIndexOfDiscNumber);
            }
            final Integer _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            }
            final Integer _tmpBitrate;
            if (_cursor.isNull(_cursorIndexOfBitrate)) {
              _tmpBitrate = null;
            } else {
              _tmpBitrate = _cursor.getInt(_cursorIndexOfBitrate);
            }
            final Integer _tmpSampleRate;
            if (_cursor.isNull(_cursorIndexOfSampleRate)) {
              _tmpSampleRate = null;
            } else {
              _tmpSampleRate = _cursor.getInt(_cursorIndexOfSampleRate);
            }
            final Long _tmpFileSize;
            if (_cursor.isNull(_cursorIndexOfFileSize)) {
              _tmpFileSize = null;
            } else {
              _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            _item = new AudioFileEntity(_tmpId,_tmpTitle,_tmpArtist,_tmpAlbum,_tmpAlbumArtist,_tmpDuration,_tmpPath,_tmpType,_tmpCoverArtPath,_tmpCoverArtUri,_tmpLastPlayedPosition,_tmpLastPlayedAt,_tmpAddedDate,_tmpGenre,_tmpTrackNumber,_tmpDiscNumber,_tmpYear,_tmpBitrate,_tmpSampleRate,_tmpFileSize,_tmpMimeType,_tmpIsFavorite);
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
  public Object getTrackCountForPlaylist(final long playlistId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM playlist_tracks WHERE playlistId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
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

  @Override
  public Object getTotalDurationForPlaylist(final long playlistId,
      final Continuation<? super Long> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(af.duration), 0) FROM audio_files af\n"
            + "        INNER JOIN playlist_tracks pt ON af.id = pt.audioFileId\n"
            + "        WHERE pt.playlistId = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final long _tmp;
            _tmp = _cursor.getLong(0);
            _result = _tmp;
          } else {
            _result = 0L;
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
  public Object getPlaylistTracks(final long playlistId,
      final Continuation<? super List<PlaylistTrackCrossRef>> $completion) {
    final String _sql = "SELECT * FROM playlist_tracks WHERE playlistId = ? ORDER BY orderIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PlaylistTrackCrossRef>>() {
      @Override
      @NonNull
      public List<PlaylistTrackCrossRef> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlaylistId = CursorUtil.getColumnIndexOrThrow(_cursor, "playlistId");
          final int _cursorIndexOfAudioFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "audioFileId");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final List<PlaylistTrackCrossRef> _result = new ArrayList<PlaylistTrackCrossRef>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlaylistTrackCrossRef _item;
            final long _tmpPlaylistId;
            _tmpPlaylistId = _cursor.getLong(_cursorIndexOfPlaylistId);
            final long _tmpAudioFileId;
            _tmpAudioFileId = _cursor.getLong(_cursorIndexOfAudioFileId);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final long _tmpAddedDate;
            _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            _item = new PlaylistTrackCrossRef(_tmpPlaylistId,_tmpAudioFileId,_tmpOrderIndex,_tmpAddedDate);
            _result.add(_item);
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
  public Object getMaxOrderIndex(final long playlistId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT MAX(orderIndex) FROM playlist_tracks WHERE playlistId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
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
  public Object isTrackInPlaylist(final long playlistId, final long audioFileId,
      final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM playlist_tracks WHERE playlistId = ? AND audioFileId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, audioFileId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp != 0;
          } else {
            _result = false;
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
