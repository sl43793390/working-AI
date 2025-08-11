package com.sl.entity;

import java.util.ArrayList;
import java.util.List;

public class AgentToolExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AgentToolExample() {
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

        public Criteria andNameToolIsNull() {
            addCriterion("name_tool is null");
            return (Criteria) this;
        }

        public Criteria andNameToolIsNotNull() {
            addCriterion("name_tool is not null");
            return (Criteria) this;
        }

        public Criteria andNameToolEqualTo(String value) {
            addCriterion("name_tool =", value, "nameTool");
            return (Criteria) this;
        }

        public Criteria andNameToolNotEqualTo(String value) {
            addCriterion("name_tool <>", value, "nameTool");
            return (Criteria) this;
        }

        public Criteria andNameToolGreaterThan(String value) {
            addCriterion("name_tool >", value, "nameTool");
            return (Criteria) this;
        }

        public Criteria andNameToolGreaterThanOrEqualTo(String value) {
            addCriterion("name_tool >=", value, "nameTool");
            return (Criteria) this;
        }

        public Criteria andNameToolLessThan(String value) {
            addCriterion("name_tool <", value, "nameTool");
            return (Criteria) this;
        }

        public Criteria andNameToolLessThanOrEqualTo(String value) {
            addCriterion("name_tool <=", value, "nameTool");
            return (Criteria) this;
        }

        public Criteria andNameToolLike(String value) {
            addCriterion("name_tool like", value, "nameTool");
            return (Criteria) this;
        }

        public Criteria andNameToolNotLike(String value) {
            addCriterion("name_tool not like", value, "nameTool");
            return (Criteria) this;
        }

        public Criteria andNameToolIn(List<String> values) {
            addCriterion("name_tool in", values, "nameTool");
            return (Criteria) this;
        }

        public Criteria andNameToolNotIn(List<String> values) {
            addCriterion("name_tool not in", values, "nameTool");
            return (Criteria) this;
        }

        public Criteria andNameToolBetween(String value1, String value2) {
            addCriterion("name_tool between", value1, value2, "nameTool");
            return (Criteria) this;
        }

        public Criteria andNameToolNotBetween(String value1, String value2) {
            addCriterion("name_tool not between", value1, value2, "nameTool");
            return (Criteria) this;
        }

        public Criteria andNameMcpIsNull() {
            addCriterion("name_mcp is null");
            return (Criteria) this;
        }

        public Criteria andNameMcpIsNotNull() {
            addCriterion("name_mcp is not null");
            return (Criteria) this;
        }

        public Criteria andNameMcpEqualTo(String value) {
            addCriterion("name_mcp =", value, "nameMcp");
            return (Criteria) this;
        }

        public Criteria andNameMcpNotEqualTo(String value) {
            addCriterion("name_mcp <>", value, "nameMcp");
            return (Criteria) this;
        }

        public Criteria andNameMcpGreaterThan(String value) {
            addCriterion("name_mcp >", value, "nameMcp");
            return (Criteria) this;
        }

        public Criteria andNameMcpGreaterThanOrEqualTo(String value) {
            addCriterion("name_mcp >=", value, "nameMcp");
            return (Criteria) this;
        }

        public Criteria andNameMcpLessThan(String value) {
            addCriterion("name_mcp <", value, "nameMcp");
            return (Criteria) this;
        }

        public Criteria andNameMcpLessThanOrEqualTo(String value) {
            addCriterion("name_mcp <=", value, "nameMcp");
            return (Criteria) this;
        }

        public Criteria andNameMcpLike(String value) {
            addCriterion("name_mcp like", value, "nameMcp");
            return (Criteria) this;
        }

        public Criteria andNameMcpNotLike(String value) {
            addCriterion("name_mcp not like", value, "nameMcp");
            return (Criteria) this;
        }

        public Criteria andNameMcpIn(List<String> values) {
            addCriterion("name_mcp in", values, "nameMcp");
            return (Criteria) this;
        }

        public Criteria andNameMcpNotIn(List<String> values) {
            addCriterion("name_mcp not in", values, "nameMcp");
            return (Criteria) this;
        }

        public Criteria andNameMcpBetween(String value1, String value2) {
            addCriterion("name_mcp between", value1, value2, "nameMcp");
            return (Criteria) this;
        }

        public Criteria andNameMcpNotBetween(String value1, String value2) {
            addCriterion("name_mcp not between", value1, value2, "nameMcp");
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