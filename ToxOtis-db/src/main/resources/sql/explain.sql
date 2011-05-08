EXPLAIN SELECT featureUri FROM ModelDepFeatures ORDER BY idx;
EXPLAIN SELECT COUNT(Model.id) FROM Model INNER JOIN OTComponent ON Model.id=OTComponent.id  WHERE OTComponent.enabled=true