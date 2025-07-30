package com.sl.entity;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeBaseExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public KnowledgeBaseExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andUserIdIsNull() {
            addCriterion("USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("USER_ID =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("USER_ID <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("USER_ID >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("USER_ID >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("USER_ID <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("USER_ID <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("USER_ID like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("USER_ID not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("USER_ID in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("USER_ID not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("USER_ID between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("USER_ID not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andNameBaseIsNull() {
            addCriterion("NAME_BASE is null");
            return (Criteria) this;
        }

        public Criteria andNameBaseIsNotNull() {
            addCriterion("NAME_BASE is not null");
            return (Criteria) this;
        }

        public Criteria andNameBaseEqualTo(String value) {
            addCriterion("NAME_BASE =", value, "nameBase");
            return (Criteria) this;
        }

        public Criteria andNameBaseNotEqualTo(String value) {
            addCriterion("NAME_BASE <>", value, "nameBase");
            return (Criteria) this;
        }

        public Criteria andNameBaseGreaterThan(String value) {
            addCriterion("NAME_BASE >", value, "nameBase");
            return (Criteria) this;
        }

        public Criteria andNameBaseGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_BASE >=", value, "nameBase");
            return (Criteria) this;
        }

        public Criteria andNameBaseLessThan(String value) {
            addCriterion("NAME_BASE <", value, "nameBase");
            return (Criteria) this;
        }

        public Criteria andNameBaseLessThanOrEqualTo(String value) {
            addCriterion("NAME_BASE <=", value, "nameBase");
            return (Criteria) this;
        }

        public Criteria andNameBaseLike(String value) {
            addCriterion("NAME_BASE like", value, "nameBase");
            return (Criteria) this;
        }

        public Criteria andNameBaseNotLike(String value) {
            addCriterion("NAME_BASE not like", value, "nameBase");
            return (Criteria) this;
        }

        public Criteria andNameBaseIn(List<String> values) {
            addCriterion("NAME_BASE in", values, "nameBase");
            return (Criteria) this;
        }

        public Criteria andNameBaseNotIn(List<String> values) {
            addCriterion("NAME_BASE not in", values, "nameBase");
            return (Criteria) this;
        }

        public Criteria andNameBaseBetween(String value1, String value2) {
            addCriterion("NAME_BASE between", value1, value2, "nameBase");
            return (Criteria) this;
        }

        public Criteria andNameBaseNotBetween(String value1, String value2) {
            addCriterion("NAME_BASE not between", value1, value2, "nameBase");
            return (Criteria) this;
        }

        public Criteria andIdBaseIsNull() {
            addCriterion("ID_BASE is null");
            return (Criteria) this;
        }

        public Criteria andIdBaseIsNotNull() {
            addCriterion("ID_BASE is not null");
            return (Criteria) this;
        }

        public Criteria andIdBaseEqualTo(String value) {
            addCriterion("ID_BASE =", value, "idBase");
            return (Criteria) this;
        }

        public Criteria andIdBaseNotEqualTo(String value) {
            addCriterion("ID_BASE <>", value, "idBase");
            return (Criteria) this;
        }

        public Criteria andIdBaseGreaterThan(String value) {
            addCriterion("ID_BASE >", value, "idBase");
            return (Criteria) this;
        }

        public Criteria andIdBaseGreaterThanOrEqualTo(String value) {
            addCriterion("ID_BASE >=", value, "idBase");
            return (Criteria) this;
        }

        public Criteria andIdBaseLessThan(String value) {
            addCriterion("ID_BASE <", value, "idBase");
            return (Criteria) this;
        }

        public Criteria andIdBaseLessThanOrEqualTo(String value) {
            addCriterion("ID_BASE <=", value, "idBase");
            return (Criteria) this;
        }

        public Criteria andIdBaseLike(String value) {
            addCriterion("ID_BASE like", value, "idBase");
            return (Criteria) this;
        }

        public Criteria andIdBaseNotLike(String value) {
            addCriterion("ID_BASE not like", value, "idBase");
            return (Criteria) this;
        }

        public Criteria andIdBaseIn(List<String> values) {
            addCriterion("ID_BASE in", values, "idBase");
            return (Criteria) this;
        }

        public Criteria andIdBaseNotIn(List<String> values) {
            addCriterion("ID_BASE not in", values, "idBase");
            return (Criteria) this;
        }

        public Criteria andIdBaseBetween(String value1, String value2) {
            addCriterion("ID_BASE between", value1, value2, "idBase");
            return (Criteria) this;
        }

        public Criteria andIdBaseNotBetween(String value1, String value2) {
            addCriterion("ID_BASE not between", value1, value2, "idBase");
            return (Criteria) this;
        }

        public Criteria andDescBaseIsNull() {
            addCriterion("DESC_BASE is null");
            return (Criteria) this;
        }

        public Criteria andDescBaseIsNotNull() {
            addCriterion("DESC_BASE is not null");
            return (Criteria) this;
        }

        public Criteria andDescBaseEqualTo(String value) {
            addCriterion("DESC_BASE =", value, "descBase");
            return (Criteria) this;
        }

        public Criteria andDescBaseNotEqualTo(String value) {
            addCriterion("DESC_BASE <>", value, "descBase");
            return (Criteria) this;
        }

        public Criteria andDescBaseGreaterThan(String value) {
            addCriterion("DESC_BASE >", value, "descBase");
            return (Criteria) this;
        }

        public Criteria andDescBaseGreaterThanOrEqualTo(String value) {
            addCriterion("DESC_BASE >=", value, "descBase");
            return (Criteria) this;
        }

        public Criteria andDescBaseLessThan(String value) {
            addCriterion("DESC_BASE <", value, "descBase");
            return (Criteria) this;
        }

        public Criteria andDescBaseLessThanOrEqualTo(String value) {
            addCriterion("DESC_BASE <=", value, "descBase");
            return (Criteria) this;
        }

        public Criteria andDescBaseLike(String value) {
            addCriterion("DESC_BASE like", value, "descBase");
            return (Criteria) this;
        }

        public Criteria andDescBaseNotLike(String value) {
            addCriterion("DESC_BASE not like", value, "descBase");
            return (Criteria) this;
        }

        public Criteria andDescBaseIn(List<String> values) {
            addCriterion("DESC_BASE in", values, "descBase");
            return (Criteria) this;
        }

        public Criteria andDescBaseNotIn(List<String> values) {
            addCriterion("DESC_BASE not in", values, "descBase");
            return (Criteria) this;
        }

        public Criteria andDescBaseBetween(String value1, String value2) {
            addCriterion("DESC_BASE between", value1, value2, "descBase");
            return (Criteria) this;
        }

        public Criteria andDescBaseNotBetween(String value1, String value2) {
            addCriterion("DESC_BASE not between", value1, value2, "descBase");
            return (Criteria) this;
        }

        public Criteria andNamePathIsNull() {
            addCriterion("NAME_PATH is null");
            return (Criteria) this;
        }

        public Criteria andNamePathIsNotNull() {
            addCriterion("NAME_PATH is not null");
            return (Criteria) this;
        }

        public Criteria andNamePathEqualTo(String value) {
            addCriterion("NAME_PATH =", value, "namePath");
            return (Criteria) this;
        }

        public Criteria andNamePathNotEqualTo(String value) {
            addCriterion("NAME_PATH <>", value, "namePath");
            return (Criteria) this;
        }

        public Criteria andNamePathGreaterThan(String value) {
            addCriterion("NAME_PATH >", value, "namePath");
            return (Criteria) this;
        }

        public Criteria andNamePathGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_PATH >=", value, "namePath");
            return (Criteria) this;
        }

        public Criteria andNamePathLessThan(String value) {
            addCriterion("NAME_PATH <", value, "namePath");
            return (Criteria) this;
        }

        public Criteria andNamePathLessThanOrEqualTo(String value) {
            addCriterion("NAME_PATH <=", value, "namePath");
            return (Criteria) this;
        }

        public Criteria andNamePathLike(String value) {
            addCriterion("NAME_PATH like", value, "namePath");
            return (Criteria) this;
        }

        public Criteria andNamePathNotLike(String value) {
            addCriterion("NAME_PATH not like", value, "namePath");
            return (Criteria) this;
        }

        public Criteria andNamePathIn(List<String> values) {
            addCriterion("NAME_PATH in", values, "namePath");
            return (Criteria) this;
        }

        public Criteria andNamePathNotIn(List<String> values) {
            addCriterion("NAME_PATH not in", values, "namePath");
            return (Criteria) this;
        }

        public Criteria andNamePathBetween(String value1, String value2) {
            addCriterion("NAME_PATH between", value1, value2, "namePath");
            return (Criteria) this;
        }

        public Criteria andNamePathNotBetween(String value1, String value2) {
            addCriterion("NAME_PATH not between", value1, value2, "namePath");
            return (Criteria) this;
        }

        public Criteria andNameCollectionIsNull() {
            addCriterion("NAME_COLLECTION is null");
            return (Criteria) this;
        }

        public Criteria andNameCollectionIsNotNull() {
            addCriterion("NAME_COLLECTION is not null");
            return (Criteria) this;
        }

        public Criteria andNameCollectionEqualTo(String value) {
            addCriterion("NAME_COLLECTION =", value, "nameCollection");
            return (Criteria) this;
        }

        public Criteria andNameCollectionNotEqualTo(String value) {
            addCriterion("NAME_COLLECTION <>", value, "nameCollection");
            return (Criteria) this;
        }

        public Criteria andNameCollectionGreaterThan(String value) {
            addCriterion("NAME_COLLECTION >", value, "nameCollection");
            return (Criteria) this;
        }

        public Criteria andNameCollectionGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_COLLECTION >=", value, "nameCollection");
            return (Criteria) this;
        }

        public Criteria andNameCollectionLessThan(String value) {
            addCriterion("NAME_COLLECTION <", value, "nameCollection");
            return (Criteria) this;
        }

        public Criteria andNameCollectionLessThanOrEqualTo(String value) {
            addCriterion("NAME_COLLECTION <=", value, "nameCollection");
            return (Criteria) this;
        }

        public Criteria andNameCollectionLike(String value) {
            addCriterion("NAME_COLLECTION like", value, "nameCollection");
            return (Criteria) this;
        }

        public Criteria andNameCollectionNotLike(String value) {
            addCriterion("NAME_COLLECTION not like", value, "nameCollection");
            return (Criteria) this;
        }

        public Criteria andNameCollectionIn(List<String> values) {
            addCriterion("NAME_COLLECTION in", values, "nameCollection");
            return (Criteria) this;
        }

        public Criteria andNameCollectionNotIn(List<String> values) {
            addCriterion("NAME_COLLECTION not in", values, "nameCollection");
            return (Criteria) this;
        }

        public Criteria andNameCollectionBetween(String value1, String value2) {
            addCriterion("NAME_COLLECTION between", value1, value2, "nameCollection");
            return (Criteria) this;
        }

        public Criteria andNameCollectionNotBetween(String value1, String value2) {
            addCriterion("NAME_COLLECTION not between", value1, value2, "nameCollection");
            return (Criteria) this;
        }

        public Criteria andDbTypeIsNull() {
            addCriterion("DB_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andDbTypeIsNotNull() {
            addCriterion("DB_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andDbTypeEqualTo(String value) {
            addCriterion("DB_TYPE =", value, "dbType");
            return (Criteria) this;
        }

        public Criteria andDbTypeNotEqualTo(String value) {
            addCriterion("DB_TYPE <>", value, "dbType");
            return (Criteria) this;
        }

        public Criteria andDbTypeGreaterThan(String value) {
            addCriterion("DB_TYPE >", value, "dbType");
            return (Criteria) this;
        }

        public Criteria andDbTypeGreaterThanOrEqualTo(String value) {
            addCriterion("DB_TYPE >=", value, "dbType");
            return (Criteria) this;
        }

        public Criteria andDbTypeLessThan(String value) {
            addCriterion("DB_TYPE <", value, "dbType");
            return (Criteria) this;
        }

        public Criteria andDbTypeLessThanOrEqualTo(String value) {
            addCriterion("DB_TYPE <=", value, "dbType");
            return (Criteria) this;
        }

        public Criteria andDbTypeLike(String value) {
            addCriterion("DB_TYPE like", value, "dbType");
            return (Criteria) this;
        }

        public Criteria andDbTypeNotLike(String value) {
            addCriterion("DB_TYPE not like", value, "dbType");
            return (Criteria) this;
        }

        public Criteria andDbTypeIn(List<String> values) {
            addCriterion("DB_TYPE in", values, "dbType");
            return (Criteria) this;
        }

        public Criteria andDbTypeNotIn(List<String> values) {
            addCriterion("DB_TYPE not in", values, "dbType");
            return (Criteria) this;
        }

        public Criteria andDbTypeBetween(String value1, String value2) {
            addCriterion("DB_TYPE between", value1, value2, "dbType");
            return (Criteria) this;
        }

        public Criteria andDbTypeNotBetween(String value1, String value2) {
            addCriterion("DB_TYPE not between", value1, value2, "dbType");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeIsNull() {
            addCriterion("SEGMENTED_MODE is null");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeIsNotNull() {
            addCriterion("SEGMENTED_MODE is not null");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeEqualTo(String value) {
            addCriterion("SEGMENTED_MODE =", value, "segmentedMode");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeNotEqualTo(String value) {
            addCriterion("SEGMENTED_MODE <>", value, "segmentedMode");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeGreaterThan(String value) {
            addCriterion("SEGMENTED_MODE >", value, "segmentedMode");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeGreaterThanOrEqualTo(String value) {
            addCriterion("SEGMENTED_MODE >=", value, "segmentedMode");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeLessThan(String value) {
            addCriterion("SEGMENTED_MODE <", value, "segmentedMode");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeLessThanOrEqualTo(String value) {
            addCriterion("SEGMENTED_MODE <=", value, "segmentedMode");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeLike(String value) {
            addCriterion("SEGMENTED_MODE like", value, "segmentedMode");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeNotLike(String value) {
            addCriterion("SEGMENTED_MODE not like", value, "segmentedMode");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeIn(List<String> values) {
            addCriterion("SEGMENTED_MODE in", values, "segmentedMode");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeNotIn(List<String> values) {
            addCriterion("SEGMENTED_MODE not in", values, "segmentedMode");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeBetween(String value1, String value2) {
            addCriterion("SEGMENTED_MODE between", value1, value2, "segmentedMode");
            return (Criteria) this;
        }

        public Criteria andSegmentedModeNotBetween(String value1, String value2) {
            addCriterion("SEGMENTED_MODE not between", value1, value2, "segmentedMode");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelIsNull() {
            addCriterion("EMBEDDING_MODEL is null");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelIsNotNull() {
            addCriterion("EMBEDDING_MODEL is not null");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelEqualTo(String value) {
            addCriterion("EMBEDDING_MODEL =", value, "embeddingModel");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelNotEqualTo(String value) {
            addCriterion("EMBEDDING_MODEL <>", value, "embeddingModel");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelGreaterThan(String value) {
            addCriterion("EMBEDDING_MODEL >", value, "embeddingModel");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelGreaterThanOrEqualTo(String value) {
            addCriterion("EMBEDDING_MODEL >=", value, "embeddingModel");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelLessThan(String value) {
            addCriterion("EMBEDDING_MODEL <", value, "embeddingModel");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelLessThanOrEqualTo(String value) {
            addCriterion("EMBEDDING_MODEL <=", value, "embeddingModel");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelLike(String value) {
            addCriterion("EMBEDDING_MODEL like", value, "embeddingModel");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelNotLike(String value) {
            addCriterion("EMBEDDING_MODEL not like", value, "embeddingModel");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelIn(List<String> values) {
            addCriterion("EMBEDDING_MODEL in", values, "embeddingModel");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelNotIn(List<String> values) {
            addCriterion("EMBEDDING_MODEL not in", values, "embeddingModel");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelBetween(String value1, String value2) {
            addCriterion("EMBEDDING_MODEL between", value1, value2, "embeddingModel");
            return (Criteria) this;
        }

        public Criteria andEmbeddingModelNotBetween(String value1, String value2) {
            addCriterion("EMBEDDING_MODEL not between", value1, value2, "embeddingModel");
            return (Criteria) this;
        }

        public Criteria andSearchTypeIsNull() {
            addCriterion("SEARCH_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andSearchTypeIsNotNull() {
            addCriterion("SEARCH_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andSearchTypeEqualTo(String value) {
            addCriterion("SEARCH_TYPE =", value, "searchType");
            return (Criteria) this;
        }

        public Criteria andSearchTypeNotEqualTo(String value) {
            addCriterion("SEARCH_TYPE <>", value, "searchType");
            return (Criteria) this;
        }

        public Criteria andSearchTypeGreaterThan(String value) {
            addCriterion("SEARCH_TYPE >", value, "searchType");
            return (Criteria) this;
        }

        public Criteria andSearchTypeGreaterThanOrEqualTo(String value) {
            addCriterion("SEARCH_TYPE >=", value, "searchType");
            return (Criteria) this;
        }

        public Criteria andSearchTypeLessThan(String value) {
            addCriterion("SEARCH_TYPE <", value, "searchType");
            return (Criteria) this;
        }

        public Criteria andSearchTypeLessThanOrEqualTo(String value) {
            addCriterion("SEARCH_TYPE <=", value, "searchType");
            return (Criteria) this;
        }

        public Criteria andSearchTypeLike(String value) {
            addCriterion("SEARCH_TYPE like", value, "searchType");
            return (Criteria) this;
        }

        public Criteria andSearchTypeNotLike(String value) {
            addCriterion("SEARCH_TYPE not like", value, "searchType");
            return (Criteria) this;
        }

        public Criteria andSearchTypeIn(List<String> values) {
            addCriterion("SEARCH_TYPE in", values, "searchType");
            return (Criteria) this;
        }

        public Criteria andSearchTypeNotIn(List<String> values) {
            addCriterion("SEARCH_TYPE not in", values, "searchType");
            return (Criteria) this;
        }

        public Criteria andSearchTypeBetween(String value1, String value2) {
            addCriterion("SEARCH_TYPE between", value1, value2, "searchType");
            return (Criteria) this;
        }

        public Criteria andSearchTypeNotBetween(String value1, String value2) {
            addCriterion("SEARCH_TYPE not between", value1, value2, "searchType");
            return (Criteria) this;
        }

        public Criteria andSegmentLengthIsNull() {
            addCriterion("SEGMENT_LENGTH is null");
            return (Criteria) this;
        }

        public Criteria andSegmentLengthIsNotNull() {
            addCriterion("SEGMENT_LENGTH is not null");
            return (Criteria) this;
        }

        public Criteria andSegmentLengthEqualTo(Integer value) {
            addCriterion("SEGMENT_LENGTH =", value, "segmentLength");
            return (Criteria) this;
        }

        public Criteria andSegmentLengthNotEqualTo(Integer value) {
            addCriterion("SEGMENT_LENGTH <>", value, "segmentLength");
            return (Criteria) this;
        }

        public Criteria andSegmentLengthGreaterThan(Integer value) {
            addCriterion("SEGMENT_LENGTH >", value, "segmentLength");
            return (Criteria) this;
        }

        public Criteria andSegmentLengthGreaterThanOrEqualTo(Integer value) {
            addCriterion("SEGMENT_LENGTH >=", value, "segmentLength");
            return (Criteria) this;
        }

        public Criteria andSegmentLengthLessThan(Integer value) {
            addCriterion("SEGMENT_LENGTH <", value, "segmentLength");
            return (Criteria) this;
        }

        public Criteria andSegmentLengthLessThanOrEqualTo(Integer value) {
            addCriterion("SEGMENT_LENGTH <=", value, "segmentLength");
            return (Criteria) this;
        }

        public Criteria andSegmentLengthIn(List<Integer> values) {
            addCriterion("SEGMENT_LENGTH in", values, "segmentLength");
            return (Criteria) this;
        }

        public Criteria andSegmentLengthNotIn(List<Integer> values) {
            addCriterion("SEGMENT_LENGTH not in", values, "segmentLength");
            return (Criteria) this;
        }

        public Criteria andSegmentLengthBetween(Integer value1, Integer value2) {
            addCriterion("SEGMENT_LENGTH between", value1, value2, "segmentLength");
            return (Criteria) this;
        }

        public Criteria andSegmentLengthNotBetween(Integer value1, Integer value2) {
            addCriterion("SEGMENT_LENGTH not between", value1, value2, "segmentLength");
            return (Criteria) this;
        }

        public Criteria andSegmentOverlapIsNull() {
            addCriterion("SEGMENT_OVERLAP is null");
            return (Criteria) this;
        }

        public Criteria andSegmentOverlapIsNotNull() {
            addCriterion("SEGMENT_OVERLAP is not null");
            return (Criteria) this;
        }

        public Criteria andSegmentOverlapEqualTo(Integer value) {
            addCriterion("SEGMENT_OVERLAP =", value, "segmentOverlap");
            return (Criteria) this;
        }

        public Criteria andSegmentOverlapNotEqualTo(Integer value) {
            addCriterion("SEGMENT_OVERLAP <>", value, "segmentOverlap");
            return (Criteria) this;
        }

        public Criteria andSegmentOverlapGreaterThan(Integer value) {
            addCriterion("SEGMENT_OVERLAP >", value, "segmentOverlap");
            return (Criteria) this;
        }

        public Criteria andSegmentOverlapGreaterThanOrEqualTo(Integer value) {
            addCriterion("SEGMENT_OVERLAP >=", value, "segmentOverlap");
            return (Criteria) this;
        }

        public Criteria andSegmentOverlapLessThan(Integer value) {
            addCriterion("SEGMENT_OVERLAP <", value, "segmentOverlap");
            return (Criteria) this;
        }

        public Criteria andSegmentOverlapLessThanOrEqualTo(Integer value) {
            addCriterion("SEGMENT_OVERLAP <=", value, "segmentOverlap");
            return (Criteria) this;
        }

        public Criteria andSegmentOverlapIn(List<Integer> values) {
            addCriterion("SEGMENT_OVERLAP in", values, "segmentOverlap");
            return (Criteria) this;
        }

        public Criteria andSegmentOverlapNotIn(List<Integer> values) {
            addCriterion("SEGMENT_OVERLAP not in", values, "segmentOverlap");
            return (Criteria) this;
        }

        public Criteria andSegmentOverlapBetween(Integer value1, Integer value2) {
            addCriterion("SEGMENT_OVERLAP between", value1, value2, "segmentOverlap");
            return (Criteria) this;
        }

        public Criteria andSegmentOverlapNotBetween(Integer value1, Integer value2) {
            addCriterion("SEGMENT_OVERLAP not between", value1, value2, "segmentOverlap");
            return (Criteria) this;
        }

        public Criteria andFlagRerankIsNull() {
            addCriterion("FLAG_RERANK is null");
            return (Criteria) this;
        }

        public Criteria andFlagRerankIsNotNull() {
            addCriterion("FLAG_RERANK is not null");
            return (Criteria) this;
        }

        public Criteria andFlagRerankEqualTo(String value) {
            addCriterion("FLAG_RERANK =", value, "flagRerank");
            return (Criteria) this;
        }

        public Criteria andFlagRerankNotEqualTo(String value) {
            addCriterion("FLAG_RERANK <>", value, "flagRerank");
            return (Criteria) this;
        }

        public Criteria andFlagRerankGreaterThan(String value) {
            addCriterion("FLAG_RERANK >", value, "flagRerank");
            return (Criteria) this;
        }

        public Criteria andFlagRerankGreaterThanOrEqualTo(String value) {
            addCriterion("FLAG_RERANK >=", value, "flagRerank");
            return (Criteria) this;
        }

        public Criteria andFlagRerankLessThan(String value) {
            addCriterion("FLAG_RERANK <", value, "flagRerank");
            return (Criteria) this;
        }

        public Criteria andFlagRerankLessThanOrEqualTo(String value) {
            addCriterion("FLAG_RERANK <=", value, "flagRerank");
            return (Criteria) this;
        }

        public Criteria andFlagRerankLike(String value) {
            addCriterion("FLAG_RERANK like", value, "flagRerank");
            return (Criteria) this;
        }

        public Criteria andFlagRerankNotLike(String value) {
            addCriterion("FLAG_RERANK not like", value, "flagRerank");
            return (Criteria) this;
        }

        public Criteria andFlagRerankIn(List<String> values) {
            addCriterion("FLAG_RERANK in", values, "flagRerank");
            return (Criteria) this;
        }

        public Criteria andFlagRerankNotIn(List<String> values) {
            addCriterion("FLAG_RERANK not in", values, "flagRerank");
            return (Criteria) this;
        }

        public Criteria andFlagRerankBetween(String value1, String value2) {
            addCriterion("FLAG_RERANK between", value1, value2, "flagRerank");
            return (Criteria) this;
        }

        public Criteria andFlagRerankNotBetween(String value1, String value2) {
            addCriterion("FLAG_RERANK not between", value1, value2, "flagRerank");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}