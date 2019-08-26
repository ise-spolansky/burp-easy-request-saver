package com.securityevaluators.burpeasyrequestsaver;

public enum DataType {
    HEADER("Request", "Requests"),
    BODY("Body", "Bodies");

    private String _singularNoun;
    private String _pluralNoun;

    DataType(String singularNoun, String pluralNoun) {
        _singularNoun = singularNoun;
        _pluralNoun = pluralNoun;
    }

    public String getNoun(boolean plural) {
        return plural ? _pluralNoun : _singularNoun;
    }
}
