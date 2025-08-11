package com.sl.entity;

import java.util.ArrayList;
import java.util.List;

public class UserAgentExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserAgentExample() {
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
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andIdAgentIsNull() {
            addCriterion("id_agent is null");
            return (Criteria) this;
        }

        public Criteria andIdAgentIsNotNull() {
            addCriterion("id_agent is not null");
            return (Criteria) this;
        }

        public Criteria andIdAgentEqualTo(String value) {
            addCriterion("id_agent =", value, "idAgent");
            return (Criteria) this;
        }

        public Criteria andIdAgentNotEqualTo(String value) {
            addCriterion("id_agent <>", value, "idAgent");
            return (Criteria) this;
        }

        public Criteria andIdAgentGreaterThan(String value) {
            addCriterion("id_agent >", value, "idAgent");
            return (Criteria) this;
        }

        public Criteria andIdAgentGreaterThanOrEqualTo(String value) {
            addCriterion("id_agent >=", value, "idAgent");
            return (Criteria) this;
        }

        public Criteria andIdAgentLessThan(String value) {
            addCriterion("id_agent <", value, "idAgent");
            return (Criteria) this;
        }

        public Criteria andIdAgentLessThanOrEqualTo(String value) {
            addCriterion("id_agent <=", value, "idAgent");
            return (Criteria) this;
        }

        public Criteria andIdAgentLike(String value) {
            addCriterion("id_agent like", value, "idAgent");
            return (Criteria) this;
        }

        public Criteria andIdAgentNotLike(String value) {
            addCriterion("id_agent not like", value, "idAgent");
            return (Criteria) this;
        }

        public Criteria andIdAgentIn(List<String> values) {
            addCriterion("id_agent in", values, "idAgent");
            return (Criteria) this;
        }

        public Criteria andIdAgentNotIn(List<String> values) {
            addCriterion("id_agent not in", values, "idAgent");
            return (Criteria) this;
        }

        public Criteria andIdAgentBetween(String value1, String value2) {
            addCriterion("id_agent between", value1, value2, "idAgent");
            return (Criteria) this;
        }

        public Criteria andIdAgentNotBetween(String value1, String value2) {
            addCriterion("id_agent not between", value1, value2, "idAgent");
            return (Criteria) this;
        }

        public Criteria andNameAgentIsNull() {
            addCriterion("name_agent is null");
            return (Criteria) this;
        }

        public Criteria andNameAgentIsNotNull() {
            addCriterion("name_agent is not null");
            return (Criteria) this;
        }

        public Criteria andNameAgentEqualTo(String value) {
            addCriterion("name_agent =", value, "nameAgent");
            return (Criteria) this;
        }

        public Criteria andNameAgentNotEqualTo(String value) {
            addCriterion("name_agent <>", value, "nameAgent");
            return (Criteria) this;
        }

        public Criteria andNameAgentGreaterThan(String value) {
            addCriterion("name_agent >", value, "nameAgent");
            return (Criteria) this;
        }

        public Criteria andNameAgentGreaterThanOrEqualTo(String value) {
            addCriterion("name_agent >=", value, "nameAgent");
            return (Criteria) this;
        }

        public Criteria andNameAgentLessThan(String value) {
            addCriterion("name_agent <", value, "nameAgent");
            return (Criteria) this;
        }

        public Criteria andNameAgentLessThanOrEqualTo(String value) {
            addCriterion("name_agent <=", value, "nameAgent");
            return (Criteria) this;
        }

        public Criteria andNameAgentLike(String value) {
            addCriterion("name_agent like", value, "nameAgent");
            return (Criteria) this;
        }

        public Criteria andNameAgentNotLike(String value) {
            addCriterion("name_agent not like", value, "nameAgent");
            return (Criteria) this;
        }

        public Criteria andNameAgentIn(List<String> values) {
            addCriterion("name_agent in", values, "nameAgent");
            return (Criteria) this;
        }

        public Criteria andNameAgentNotIn(List<String> values) {
            addCriterion("name_agent not in", values, "nameAgent");
            return (Criteria) this;
        }

        public Criteria andNameAgentBetween(String value1, String value2) {
            addCriterion("name_agent between", value1, value2, "nameAgent");
            return (Criteria) this;
        }

        public Criteria andNameAgentNotBetween(String value1, String value2) {
            addCriterion("name_agent not between", value1, value2, "nameAgent");
            return (Criteria) this;
        }

        public Criteria andSystemPromptIsNull() {
            addCriterion("system_prompt is null");
            return (Criteria) this;
        }

        public Criteria andSystemPromptIsNotNull() {
            addCriterion("system_prompt is not null");
            return (Criteria) this;
        }

        public Criteria andSystemPromptEqualTo(String value) {
            addCriterion("system_prompt =", value, "systemPrompt");
            return (Criteria) this;
        }

        public Criteria andSystemPromptNotEqualTo(String value) {
            addCriterion("system_prompt <>", value, "systemPrompt");
            return (Criteria) this;
        }

        public Criteria andSystemPromptGreaterThan(String value) {
            addCriterion("system_prompt >", value, "systemPrompt");
            return (Criteria) this;
        }

        public Criteria andSystemPromptGreaterThanOrEqualTo(String value) {
            addCriterion("system_prompt >=", value, "systemPrompt");
            return (Criteria) this;
        }

        public Criteria andSystemPromptLessThan(String value) {
            addCriterion("system_prompt <", value, "systemPrompt");
            return (Criteria) this;
        }

        public Criteria andSystemPromptLessThanOrEqualTo(String value) {
            addCriterion("system_prompt <=", value, "systemPrompt");
            return (Criteria) this;
        }

        public Criteria andSystemPromptLike(String value) {
            addCriterion("system_prompt like", value, "systemPrompt");
            return (Criteria) this;
        }

        public Criteria andSystemPromptNotLike(String value) {
            addCriterion("system_prompt not like", value, "systemPrompt");
            return (Criteria) this;
        }

        public Criteria andSystemPromptIn(List<String> values) {
            addCriterion("system_prompt in", values, "systemPrompt");
            return (Criteria) this;
        }

        public Criteria andSystemPromptNotIn(List<String> values) {
            addCriterion("system_prompt not in", values, "systemPrompt");
            return (Criteria) this;
        }

        public Criteria andSystemPromptBetween(String value1, String value2) {
            addCriterion("system_prompt between", value1, value2, "systemPrompt");
            return (Criteria) this;
        }

        public Criteria andSystemPromptNotBetween(String value1, String value2) {
            addCriterion("system_prompt not between", value1, value2, "systemPrompt");
            return (Criteria) this;
        }

        public Criteria andCdDescIsNull() {
            addCriterion("cd_desc is null");
            return (Criteria) this;
        }

        public Criteria andCdDescIsNotNull() {
            addCriterion("cd_desc is not null");
            return (Criteria) this;
        }

        public Criteria andCdDescEqualTo(String value) {
            addCriterion("cd_desc =", value, "cdDesc");
            return (Criteria) this;
        }

        public Criteria andCdDescNotEqualTo(String value) {
            addCriterion("cd_desc <>", value, "cdDesc");
            return (Criteria) this;
        }

        public Criteria andCdDescGreaterThan(String value) {
            addCriterion("cd_desc >", value, "cdDesc");
            return (Criteria) this;
        }

        public Criteria andCdDescGreaterThanOrEqualTo(String value) {
            addCriterion("cd_desc >=", value, "cdDesc");
            return (Criteria) this;
        }

        public Criteria andCdDescLessThan(String value) {
            addCriterion("cd_desc <", value, "cdDesc");
            return (Criteria) this;
        }

        public Criteria andCdDescLessThanOrEqualTo(String value) {
            addCriterion("cd_desc <=", value, "cdDesc");
            return (Criteria) this;
        }

        public Criteria andCdDescLike(String value) {
            addCriterion("cd_desc like", value, "cdDesc");
            return (Criteria) this;
        }

        public Criteria andCdDescNotLike(String value) {
            addCriterion("cd_desc not like", value, "cdDesc");
            return (Criteria) this;
        }

        public Criteria andCdDescIn(List<String> values) {
            addCriterion("cd_desc in", values, "cdDesc");
            return (Criteria) this;
        }

        public Criteria andCdDescNotIn(List<String> values) {
            addCriterion("cd_desc not in", values, "cdDesc");
            return (Criteria) this;
        }

        public Criteria andCdDescBetween(String value1, String value2) {
            addCriterion("cd_desc between", value1, value2, "cdDesc");
            return (Criteria) this;
        }

        public Criteria andCdDescNotBetween(String value1, String value2) {
            addCriterion("cd_desc not between", value1, value2, "cdDesc");
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