INSERT INTO usuario_tb (
  name,
  email,
  address,
  login,
  password,
  usuario_tipo_id
) VALUES
  (
    'User One',
    'user1@example.com',
    'Address 1',
    'user1',
    crypt('pass1', gen_salt('bf')),
    NULL
  ),
  (
    'User Two',
    'user2@example.com',
    'Address 2',
    'user2',
    crypt('pass2', gen_salt('bf')),
    NULL
  );