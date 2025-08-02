package com.sl.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KnowledgeBaseFileExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public KnowledgeBaseFileExample() {
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

        public Criteria andFileNameIsNull() {
            addCriterion("FILE_NAME is null");
            return (Criteria) this;
        }

        public Criteria andFileNameIsNotNull() {
            addCriterion("FILE_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andFileNameEqualTo(String value) {
            addCriterion("FILE_NAME =", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotEqualTo(String value) {
            addCriterion("FILE_NAME <>", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameGreaterThan(String value) {
            addCriterion("FILE_NAME >", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("FILE_NAME >=", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameLessThan(String value) {
            addCriterion("FILE_NAME <", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameLessThanOrEqualTo(String value) {
            addCriterion("FILE_NAME <=", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameLike(String value) {
            addCriterion("FILE_NAME like", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotLike(String value) {
            addCriterion("FILE_NAME not like", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameIn(List<String> values) {
            addCriterion("FILE_NAME in", values, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotIn(List<String> values) {
            addCriterion("FILE_NAME not in", values, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameBetween(String value1, String value2) {
            addCriterion("FILE_NAME between", value1, value2, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotBetween(String value1, String value2) {
            addCriterion("FILE_NAME not between", value1, value2, "fileName");
            return (Criteria) this;
        }

        public Criteria andFilePathIsNull() {
            addCriterion("FILE_PATH is null");
            return (Criteria) this;
        }

        public Criteria andFilePathIsNotNull() {
            addCriterion("FILE_PATH is not null");
            return (Criteria) this;
        }

        public Criteria andFilePathEqualTo(String value) {
            addCriterion("FILE_PATH =", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathNotEqualTo(String value) {
            addCriterion("FILE_PATH <>", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathGreaterThan(String value) {
            addCriterion("FILE_PATH >", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathGreaterThanOrEqualTo(String value) {
            addCriterion("FILE_PATH >=", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathLessThan(String value) {
            addCriterion("FILE_PATH <", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathLessThanOrEqualTo(String value) {
            addCriterion("FILE_PATH <=", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathLike(String value) {
            addCriterion("FILE_PATH like", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathNotLike(String value) {
            addCriterion("FILE_PATH not like", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathIn(List<String> values) {
            addCriterion("FILE_PATH in", values, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathNotIn(List<String> values) {
            addCriterion("FILE_PATH not in", values, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathBetween(String value1, String value2) {
            addCriterion("FILE_PATH between", value1, value2, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathNotBetween(String value1, String value2) {
            addCriterion("FILE_PATH not between", value1, value2, "filePath");
            return (Criteria) this;
        }

        public Criteria andFileSizeIsNull() {
            addCriterion("FILE_SIZE is null");
            return (Criteria) this;
        }

        public Criteria andFileSizeIsNotNull() {
            addCriterion("FILE_SIZE is not null");
            return (Criteria) this;
        }

        public Criteria andFileSizeEqualTo(String value) {
            addCriterion("FILE_SIZE =", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotEqualTo(String value) {
            addCriterion("FILE_SIZE <>", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeGreaterThan(String value) {
            addCriterion("FILE_SIZE >", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeGreaterThanOrEqualTo(String value) {
            addCriterion("FILE_SIZE >=", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeLessThan(String value) {
            addCriterion("FILE_SIZE <", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeLessThanOrEqualTo(String value) {
            addCriterion("FILE_SIZE <=", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeLike(String value) {
            addCriterion("FILE_SIZE like", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotLike(String value) {
            addCriterion("FILE_SIZE not like", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeIn(List<String> values) {
            addCriterion("FILE_SIZE in", values, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotIn(List<String> values) {
            addCriterion("FILE_SIZE not in", values, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeBetween(String value1, String value2) {
            addCriterion("FILE_SIZE between", value1, value2, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotBetween(String value1, String value2) {
            addCriterion("FILE_SIZE not between", value1, value2, "fileSize");
            return (Criteria) this;
        }

        public Criteria andDtUploadIsNull() {
            addCriterion("DT_UPLOAD is null");
            return (Criteria) this;
        }

        public Criteria andDtUploadIsNotNull() {
            addCriterion("DT_UPLOAD is not null");
            return (Criteria) this;
        }

        public Criteria andDtUploadEqualTo(Date value) {
            addCriterion("DT_UPLOAD =", value, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadNotEqualTo(Date value) {
            addCriterion("DT_UPLOAD <>", value, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadGreaterThan(Date value) {
            addCriterion("DT_UPLOAD >", value, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadGreaterThanOrEqualTo(Date value) {
            addCriterion("DT_UPLOAD >=", value, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadLessThan(Date value) {
            addCriterion("DT_UPLOAD <", value, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadLessThanOrEqualTo(Date value) {
            addCriterion("DT_UPLOAD <=", value, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadIn(List<Date> values) {
            addCriterion("DT_UPLOAD in", values, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadNotIn(List<Date> values) {
            addCriterion("DT_UPLOAD not in", values, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadBetween(Date value1, Date value2) {
            addCriterion("DT_UPLOAD between", value1, value2, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadNotBetween(Date value1, Date value2) {
            addCriterion("DT_UPLOAD not between", value1, value2, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingIsNull() {
            addCriterion("FLAG_EMBEDDING is null");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingIsNotNull() {
            addCriterion("FLAG_EMBEDDING is not null");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingEqualTo(String value) {
            addCriterion("FLAG_EMBEDDING =", value, "flagEmbedding");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingNotEqualTo(String value) {
            addCriterion("FLAG_EMBEDDING <>", value, "flagEmbedding");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingGreaterThan(String value) {
            addCriterion("FLAG_EMBEDDING >", value, "flagEmbedding");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingGreaterThanOrEqualTo(String value) {
            addCriterion("FLAG_EMBEDDING >=", value, "flagEmbedding");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingLessThan(String value) {
            addCriterion("FLAG_EMBEDDING <", value, "flagEmbedding");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingLessThanOrEqualTo(String value) {
            addCriterion("FLAG_EMBEDDING <=", value, "flagEmbedding");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingLike(String value) {
            addCriterion("FLAG_EMBEDDING like", value, "flagEmbedding");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingNotLike(String value) {
            addCriterion("FLAG_EMBEDDING not like", value, "flagEmbedding");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingIn(List<String> values) {
            addCriterion("FLAG_EMBEDDING in", values, "flagEmbedding");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingNotIn(List<String> values) {
            addCriterion("FLAG_EMBEDDING not in", values, "flagEmbedding");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingBetween(String value1, String value2) {
            addCriterion("FLAG_EMBEDDING between", value1, value2, "flagEmbedding");
            return (Criteria) this;
        }

        public Criteria andFlagEmbeddingNotBetween(String value1, String value2) {
            addCriterion("FLAG_EMBEDDING not between", value1, value2, "flagEmbedding");
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