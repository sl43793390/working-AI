package com.sl.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserPromptExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserPromptExample() {
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

        public Criteria andIdPromptIsNull() {
            addCriterion("ID_PROMPT is null");
            return (Criteria) this;
        }

        public Criteria andIdPromptIsNotNull() {
            addCriterion("ID_PROMPT is not null");
            return (Criteria) this;
        }

        public Criteria andIdPromptEqualTo(String value) {
            addCriterion("ID_PROMPT =", value, "idPrompt");
            return (Criteria) this;
        }

        public Criteria andIdPromptNotEqualTo(String value) {
            addCriterion("ID_PROMPT <>", value, "idPrompt");
            return (Criteria) this;
        }

        public Criteria andIdPromptGreaterThan(String value) {
            addCriterion("ID_PROMPT >", value, "idPrompt");
            return (Criteria) this;
        }

        public Criteria andIdPromptGreaterThanOrEqualTo(String value) {
            addCriterion("ID_PROMPT >=", value, "idPrompt");
            return (Criteria) this;
        }

        public Criteria andIdPromptLessThan(String value) {
            addCriterion("ID_PROMPT <", value, "idPrompt");
            return (Criteria) this;
        }

        public Criteria andIdPromptLessThanOrEqualTo(String value) {
            addCriterion("ID_PROMPT <=", value, "idPrompt");
            return (Criteria) this;
        }

        public Criteria andIdPromptLike(String value) {
            addCriterion("ID_PROMPT like", value, "idPrompt");
            return (Criteria) this;
        }

        public Criteria andIdPromptNotLike(String value) {
            addCriterion("ID_PROMPT not like", value, "idPrompt");
            return (Criteria) this;
        }

        public Criteria andIdPromptIn(List<String> values) {
            addCriterion("ID_PROMPT in", values, "idPrompt");
            return (Criteria) this;
        }

        public Criteria andIdPromptNotIn(List<String> values) {
            addCriterion("ID_PROMPT not in", values, "idPrompt");
            return (Criteria) this;
        }

        public Criteria andIdPromptBetween(String value1, String value2) {
            addCriterion("ID_PROMPT between", value1, value2, "idPrompt");
            return (Criteria) this;
        }

        public Criteria andIdPromptNotBetween(String value1, String value2) {
            addCriterion("ID_PROMPT not between", value1, value2, "idPrompt");
            return (Criteria) this;
        }

        public Criteria andNamePromptIsNull() {
            addCriterion("NAME_PROMPT is null");
            return (Criteria) this;
        }

        public Criteria andNamePromptIsNotNull() {
            addCriterion("NAME_PROMPT is not null");
            return (Criteria) this;
        }

        public Criteria andNamePromptEqualTo(String value) {
            addCriterion("NAME_PROMPT =", value, "namePrompt");
            return (Criteria) this;
        }

        public Criteria andNamePromptNotEqualTo(String value) {
            addCriterion("NAME_PROMPT <>", value, "namePrompt");
            return (Criteria) this;
        }

        public Criteria andNamePromptGreaterThan(String value) {
            addCriterion("NAME_PROMPT >", value, "namePrompt");
            return (Criteria) this;
        }

        public Criteria andNamePromptGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_PROMPT >=", value, "namePrompt");
            return (Criteria) this;
        }

        public Criteria andNamePromptLessThan(String value) {
            addCriterion("NAME_PROMPT <", value, "namePrompt");
            return (Criteria) this;
        }

        public Criteria andNamePromptLessThanOrEqualTo(String value) {
            addCriterion("NAME_PROMPT <=", value, "namePrompt");
            return (Criteria) this;
        }

        public Criteria andNamePromptLike(String value) {
            addCriterion("NAME_PROMPT like", value, "namePrompt");
            return (Criteria) this;
        }

        public Criteria andNamePromptNotLike(String value) {
            addCriterion("NAME_PROMPT not like", value, "namePrompt");
            return (Criteria) this;
        }

        public Criteria andNamePromptIn(List<String> values) {
            addCriterion("NAME_PROMPT in", values, "namePrompt");
            return (Criteria) this;
        }

        public Criteria andNamePromptNotIn(List<String> values) {
            addCriterion("NAME_PROMPT not in", values, "namePrompt");
            return (Criteria) this;
        }

        public Criteria andNamePromptBetween(String value1, String value2) {
            addCriterion("NAME_PROMPT between", value1, value2, "namePrompt");
            return (Criteria) this;
        }

        public Criteria andNamePromptNotBetween(String value1, String value2) {
            addCriterion("NAME_PROMPT not between", value1, value2, "namePrompt");
            return (Criteria) this;
        }

        public Criteria andCdCategoryIsNull() {
            addCriterion("CD_CATEGORY is null");
            return (Criteria) this;
        }

        public Criteria andCdCategoryIsNotNull() {
            addCriterion("CD_CATEGORY is not null");
            return (Criteria) this;
        }

        public Criteria andCdCategoryEqualTo(String value) {
            addCriterion("CD_CATEGORY =", value, "cdCategory");
            return (Criteria) this;
        }

        public Criteria andCdCategoryNotEqualTo(String value) {
            addCriterion("CD_CATEGORY <>", value, "cdCategory");
            return (Criteria) this;
        }

        public Criteria andCdCategoryGreaterThan(String value) {
            addCriterion("CD_CATEGORY >", value, "cdCategory");
            return (Criteria) this;
        }

        public Criteria andCdCategoryGreaterThanOrEqualTo(String value) {
            addCriterion("CD_CATEGORY >=", value, "cdCategory");
            return (Criteria) this;
        }

        public Criteria andCdCategoryLessThan(String value) {
            addCriterion("CD_CATEGORY <", value, "cdCategory");
            return (Criteria) this;
        }

        public Criteria andCdCategoryLessThanOrEqualTo(String value) {
            addCriterion("CD_CATEGORY <=", value, "cdCategory");
            return (Criteria) this;
        }

        public Criteria andCdCategoryLike(String value) {
            addCriterion("CD_CATEGORY like", value, "cdCategory");
            return (Criteria) this;
        }

        public Criteria andCdCategoryNotLike(String value) {
            addCriterion("CD_CATEGORY not like", value, "cdCategory");
            return (Criteria) this;
        }

        public Criteria andCdCategoryIn(List<String> values) {
            addCriterion("CD_CATEGORY in", values, "cdCategory");
            return (Criteria) this;
        }

        public Criteria andCdCategoryNotIn(List<String> values) {
            addCriterion("CD_CATEGORY not in", values, "cdCategory");
            return (Criteria) this;
        }

        public Criteria andCdCategoryBetween(String value1, String value2) {
            addCriterion("CD_CATEGORY between", value1, value2, "cdCategory");
            return (Criteria) this;
        }

        public Criteria andCdCategoryNotBetween(String value1, String value2) {
            addCriterion("CD_CATEGORY not between", value1, value2, "cdCategory");
            return (Criteria) this;
        }

        public Criteria andDtCreateIsNull() {
            addCriterion("DT_CREATE is null");
            return (Criteria) this;
        }

        public Criteria andDtCreateIsNotNull() {
            addCriterion("DT_CREATE is not null");
            return (Criteria) this;
        }

        public Criteria andDtCreateEqualTo(Date value) {
            addCriterion("DT_CREATE =", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateNotEqualTo(Date value) {
            addCriterion("DT_CREATE <>", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateGreaterThan(Date value) {
            addCriterion("DT_CREATE >", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateGreaterThanOrEqualTo(Date value) {
            addCriterion("DT_CREATE >=", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateLessThan(Date value) {
            addCriterion("DT_CREATE <", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateLessThanOrEqualTo(Date value) {
            addCriterion("DT_CREATE <=", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateIn(List<Date> values) {
            addCriterion("DT_CREATE in", values, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateNotIn(List<Date> values) {
            addCriterion("DT_CREATE not in", values, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateBetween(Date value1, Date value2) {
            addCriterion("DT_CREATE between", value1, value2, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateNotBetween(Date value1, Date value2) {
            addCriterion("DT_CREATE not between", value1, value2, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andContentIsNull() {
            addCriterion("CONTENT is null");
            return (Criteria) this;
        }

        public Criteria andContentIsNotNull() {
            addCriterion("CONTENT is not null");
            return (Criteria) this;
        }

        public Criteria andContentEqualTo(String value) {
            addCriterion("CONTENT =", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotEqualTo(String value) {
            addCriterion("CONTENT <>", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThan(String value) {
            addCriterion("CONTENT >", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThanOrEqualTo(String value) {
            addCriterion("CONTENT >=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThan(String value) {
            addCriterion("CONTENT <", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThanOrEqualTo(String value) {
            addCriterion("CONTENT <=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLike(String value) {
            addCriterion("CONTENT like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotLike(String value) {
            addCriterion("CONTENT not like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentIn(List<String> values) {
            addCriterion("CONTENT in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotIn(List<String> values) {
            addCriterion("CONTENT not in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentBetween(String value1, String value2) {
            addCriterion("CONTENT between", value1, value2, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotBetween(String value1, String value2) {
            addCriterion("CONTENT not between", value1, value2, "content");
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