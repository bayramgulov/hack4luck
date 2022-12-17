CREATE SEQUENCE audio_object_id_seq;

CREATE TABLE IF NOT EXISTS audio_object
(
    id                 BIGSERIAL PRIMARY KEY,
    public_link        VARCHAR(512) NULL,
    "path"             VARCHAR(512) NOT NULL,
    audio_encoding     VARCHAR(16)  NOT NULL,
    rate_hertz         INT          NULL,
    channel_count      INT          NULL,
    recognition_status VARCHAR(32)  NOT NULL,
    created            TIMESTAMP    NOT NULL,
    operation_id       VARCHAR(255) NULL,
    recognition_text   TEXT         NULL
);

CREATE INDEX idx_audio_object_created ON audio_object (created);

ALTER TABLE audio_object
    ADD CONSTRAINT uk_audio_object_operation_id UNIQUE (operation_id);

ALTER SEQUENCE audio_object_id_seq OWNED BY audio_object.id;
