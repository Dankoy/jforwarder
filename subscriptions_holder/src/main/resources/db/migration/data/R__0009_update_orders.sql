WITH subquery AS (SELECT id
                  FROM order_types)
UPDATE dummy
SET type_id = subquery.id
FROM subquery
WHERE subquery.type = 'tag';


/*update
    orders
SET type_id = subquery.id
FROM (SELECT id
      FROM order_types
      where type = 'tag') AS subquery
;
*/
