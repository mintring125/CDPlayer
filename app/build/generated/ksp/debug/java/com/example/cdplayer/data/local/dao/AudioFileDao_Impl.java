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
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.cdplayer.data.local.entity.AudioFileEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class AudioFileDao_Impl implements AudioFileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AudioFileEntity> __insertionAdapterOfAudioFileEntity;

  private final EntityDeletionOrUpdateAdapter<AudioFileEntity> __deletionAdapterOfAudioFileEntity;

  private final EntityDeletionOrUpdateAdapter<AudioFileEntity> __updateAdapterOfAudioFileEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePlaybackPosition;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCoverArt;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public AudioFileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAudioFileEntity = new EntityInsertionAdapter<AudioFileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `audio_files` (`id`,`title`,`artist`,`album`,`albumArtist`,`duration`,`path`,`type`,`coverArtPath`,`coverArtUri`,`lastPlayedPosition`,`lastPlayedAt`,`addedDate`,`genre`,`trackNumber`,`discNumber`,`year`,`bitrate`,`sampleRate`,`fileSize`,`mimeType`,`isFavorite`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AudioFileEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getArtist() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getArtist());
        }
        if (entity.getAlbum() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getAlbum());
        }
        if (entity.getAlbumArtist() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAlbumArtist());
        }
        statement.bindLong(6, entity.getDuration());
        statement.bindString(7, entity.getPath());
        statement.bindString(8, entity.getType());
        if (entity.getCoverArtPath() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCoverArtPath());
        }
        if (entity.getCoverArtUri() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getCoverArtUri());
        }
        statement.bindLong(11, entity.getLastPlayedPosition());
        if (entity.getLastPlayedAt() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getLastPlayedAt());
        }
        statement.bindLong(13, entity.getAddedDate());
        if (entity.getGenre() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getGenre());
        }
        if (entity.getTrackNumber() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getTrackNumber());
        }
        if (entity.getDiscNumber() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getDiscNumber());
        }
        if (entity.getYear() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getYear());
        }
        if (entity.getBitrate() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getBitrate());
        }
        if (entity.getSampleRate() == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, entity.getSampleRate());
        }
        if (entity.getFileSize() == null) {
          statement.bindNull(20);
        } else {
          statement.bindLong(20, entity.getFileSize());
        }
        if (entity.getMimeType() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getMimeType());
        }
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(22, _tmp);
      }
    };
    this.__deletionAdapterOfAudioFileEntity = new EntityDeletionOrUpdateAdapter<AudioFileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `audio_files` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AudioFileEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfAudioFileEntity = new EntityDeletionOrUpdateAdapter<AudioFileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `audio_files` SET `id` = ?,`title` = ?,`artist` = ?,`album` = ?,`albumArtist` = ?,`duration` = ?,`path` = ?,`type` = ?,`coverArtPath` = ?,`coverArtUri` = ?,`lastPlayedPosition` = ?,`lastPlayedAt` = ?,`addedDate` = ?,`genre` = ?,`trackNumber` = ?,`discNumber` = ?,`year` = ?,`bitrate` = ?,`sampleRate` = ?,`fileSize` = ?,`mimeType` = ?,`isFavorite` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AudioFileEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getArtist() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getArtist());
        }
        if (entity.getAlbum() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getAlbum());
        }
        if (entity.getAlbumArtist() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAlbumArtist());
        }
        statement.bindLong(6, entity.getDuration());
        statement.bindString(7, entity.getPath());
        statement.bindString(8, entity.getType());
        if (entity.getCoverArtPath() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCoverArtPath());
        }
        if (entity.getCoverArtUri() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getCoverArtUri());
        }
        statement.bindLong(11, entity.getLastPlayedPosition());
        if (entity.getLastPlayedAt() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getLastPlayedAt());
        }
        statement.bindLong(13, entity.getAddedDate());
        if (entity.getGenre() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getGenre());
        }
        if (entity.getTrackNumber() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getTrackNumber());
        }
        if (entity.getDiscNumber() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getDiscNumber());
        }
        if (entity.getYear() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getYear());
        }
        if (entity.getBitrate() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getBitrate());
        }
        if (entity.getSampleRate() == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, entity.getSampleRate());
        }
        if (entity.getFileSize() == null) {
          statement.bindNull(20);
        } else {
          statement.bindLong(20, entity.getFileSize());
        }
        if (entity.getMimeType() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getMimeType());
        }
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(22, _tmp);
        statement.bindLong(23, entity.getId());
      }
    };
    this.__preparedStmtOfUpdatePlaybackPosition = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE audio_files SET lastPlayedPosition = ?, lastPlayedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateCoverArt = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE audio_files SET coverArtPath = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM audio_files WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final AudioFileEntity audioFile,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAudioFileEntity.insertAndReturnId(audioFile);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<AudioFileEntity> audioFiles,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAudioFileEntity.insert(audioFiles);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final AudioFileEntity audioFile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAudioFileEntity.handle(audioFile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final AudioFileEntity audioFile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAudioFileEntity.handle(audioFile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePlaybackPosition(final long id, final long position, final long playedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePlaybackPosition.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, position);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, playedAt);
        _argIndex = 3;
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
          __preparedStmtOfUpdatePlaybackPosition.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCoverArt(final long id, final String coverArtPath,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCoverArt.acquire();
        int _argIndex = 1;
        if (coverArtPath == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, coverArtPath);
        }
        _argIndex = 2;
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
          __preparedStmtOfUpdateCoverArt.release(_stmt);
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
  public Flow<List<AudioFileEntity>> getAllAudioFiles() {
    final String _sql = "SELECT * FROM audio_files ORDER BY title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<AudioFileEntity>>() {
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
  public Flow<List<AudioFileEntity>> getAudioFilesByType(final String type) {
    final String _sql = "SELECT * FROM audio_files WHERE type = ? ORDER BY title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<AudioFileEntity>>() {
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
  public Flow<List<AudioFileEntity>> getFavorites() {
    final String _sql = "SELECT * FROM audio_files WHERE isFavorite = 1 ORDER BY addedDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<AudioFileEntity>>() {
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
  public Flow<List<AudioFileEntity>> getAllMusic() {
    final String _sql = "SELECT * FROM audio_files WHERE type = 'MUSIC' ORDER BY title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<AudioFileEntity>>() {
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
  public Flow<List<AudioFileEntity>> getAllAudiobooks() {
    final String _sql = "SELECT * FROM audio_files WHERE type = 'AUDIOBOOK' ORDER BY title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<AudioFileEntity>>() {
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
  public Object getAudioFileById(final long id,
      final Continuation<? super AudioFileEntity> $completion) {
    final String _sql = "SELECT * FROM audio_files WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AudioFileEntity>() {
      @Override
      @Nullable
      public AudioFileEntity call() throws Exception {
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
          final AudioFileEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new AudioFileEntity(_tmpId,_tmpTitle,_tmpArtist,_tmpAlbum,_tmpAlbumArtist,_tmpDuration,_tmpPath,_tmpType,_tmpCoverArtPath,_tmpCoverArtUri,_tmpLastPlayedPosition,_tmpLastPlayedAt,_tmpAddedDate,_tmpGenre,_tmpTrackNumber,_tmpDiscNumber,_tmpYear,_tmpBitrate,_tmpSampleRate,_tmpFileSize,_tmpMimeType,_tmpIsFavorite);
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
  public Flow<AudioFileEntity> getAudioFileByIdFlow(final long id) {
    final String _sql = "SELECT * FROM audio_files WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<AudioFileEntity>() {
      @Override
      @Nullable
      public AudioFileEntity call() throws Exception {
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
          final AudioFileEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new AudioFileEntity(_tmpId,_tmpTitle,_tmpArtist,_tmpAlbum,_tmpAlbumArtist,_tmpDuration,_tmpPath,_tmpType,_tmpCoverArtPath,_tmpCoverArtUri,_tmpLastPlayedPosition,_tmpLastPlayedAt,_tmpAddedDate,_tmpGenre,_tmpTrackNumber,_tmpDiscNumber,_tmpYear,_tmpBitrate,_tmpSampleRate,_tmpFileSize,_tmpMimeType,_tmpIsFavorite);
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
  public Object getAudioFileByPath(final String path,
      final Continuation<? super AudioFileEntity> $completion) {
    final String _sql = "SELECT * FROM audio_files WHERE path = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, path);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AudioFileEntity>() {
      @Override
      @Nullable
      public AudioFileEntity call() throws Exception {
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
          final AudioFileEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new AudioFileEntity(_tmpId,_tmpTitle,_tmpArtist,_tmpAlbum,_tmpAlbumArtist,_tmpDuration,_tmpPath,_tmpType,_tmpCoverArtPath,_tmpCoverArtUri,_tmpLastPlayedPosition,_tmpLastPlayedAt,_tmpAddedDate,_tmpGenre,_tmpTrackNumber,_tmpDiscNumber,_tmpYear,_tmpBitrate,_tmpSampleRate,_tmpFileSize,_tmpMimeType,_tmpIsFavorite);
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
  public Flow<List<AudioFileEntity>> getRecentlyPlayed(final int limit) {
    final String _sql = "SELECT * FROM audio_files WHERE lastPlayedAt IS NOT NULL ORDER BY lastPlayedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<AudioFileEntity>>() {
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
  public Flow<List<String>> getAllArtists() {
    final String _sql = "SELECT DISTINCT artist FROM audio_files WHERE artist IS NOT NULL ORDER BY artist ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
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
  public Flow<List<AudioFileEntity>> getAudioFilesByArtist(final String artist) {
    final String _sql = "SELECT * FROM audio_files WHERE artist = ? ORDER BY album, trackNumber";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, artist);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<AudioFileEntity>>() {
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
  public Flow<List<String>> getAllAlbums() {
    final String _sql = "SELECT DISTINCT album FROM audio_files WHERE album IS NOT NULL ORDER BY album ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
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
  public Flow<List<AudioFileEntity>> getAudioFilesByAlbum(final String album) {
    final String _sql = "SELECT * FROM audio_files WHERE album = ? ORDER BY trackNumber ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, album);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<AudioFileEntity>>() {
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
  public Flow<List<String>> getAllGenres() {
    final String _sql = "SELECT DISTINCT genre FROM audio_files WHERE genre IS NOT NULL ORDER BY genre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
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
  public Flow<List<AudioFileEntity>> getAudioFilesByGenre(final String genre) {
    final String _sql = "SELECT * FROM audio_files WHERE genre = ? ORDER BY title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, genre);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<AudioFileEntity>>() {
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
  public Flow<List<AudioFileEntity>> searchAudioFiles(final String query) {
    final String _sql = "\n"
            + "        SELECT * FROM audio_files\n"
            + "        WHERE title LIKE '%' || ? || '%'\n"
            + "        OR artist LIKE '%' || ? || '%'\n"
            + "        OR album LIKE '%' || ? || '%'\n"
            + "        ORDER BY title ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    _argIndex = 3;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audio_files"}, new Callable<List<AudioFileEntity>>() {
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
  public Object getCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM audio_files";
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

  @Override
  public Object getCountByType(final String type, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM audio_files WHERE type = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
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
  public Object deleteNotInPaths(final List<String> existingPaths,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("DELETE FROM audio_files WHERE path NOT IN (");
        final int _inputSize = existingPaths.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (String _item : existingPaths) {
          _stmt.bindString(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
