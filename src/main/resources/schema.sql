
CREATE TABLE IF NOT EXISTS meals (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT NOT NULL,
    category    TEXT,
    description TEXT,
    rating      REAL DEFAULT 0,
    created_at  TEXT DEFAULT (datetime('now'))
);
