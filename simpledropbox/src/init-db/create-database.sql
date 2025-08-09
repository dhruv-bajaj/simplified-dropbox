DO
$$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_database WHERE datname = 'simple_dropbox'
   ) THEN
      CREATE DATABASE simple_dropbox;
   END IF;
END
$$;