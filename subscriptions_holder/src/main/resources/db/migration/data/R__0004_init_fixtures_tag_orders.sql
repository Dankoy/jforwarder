-- applied after 5.0 migration after renaming

INSERT INTO orders(NAME, value)
SELECT 'likes_count', 'top'
WHERE NOT EXISTS (SELECT name, value FROM orders WHERE name = 'likes_count' and value = 'top');

INSERT INTO orders(NAME, value)
SELECT 'newest_popular', 'top'
WHERE NOT EXISTS (SELECT name, value FROM orders WHERE name = 'newest_popular' and value = 'popular');

INSERT INTO orders(NAME, value)
SELECT 'views_count', 'views_count'
WHERE NOT EXISTS (SELECT name, value FROM orders WHERE name = 'views_count' and value = 'views_count');

INSERT INTO orders(NAME, value)
SELECT 'newest', 'fresh'
WHERE NOT EXISTS (SELECT name, value FROM orders WHERE name = 'newest' and value = 'fresh');