CREATE CONSTRAINT `resource_id_uniq` IF NOT EXISTS
FOR (r: `Resource`)
REQUIRE (r.`id`) IS UNIQUE;