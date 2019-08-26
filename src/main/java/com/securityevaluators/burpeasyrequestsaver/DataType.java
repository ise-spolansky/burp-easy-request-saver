package com.securityevaluators.burpeasyrequestsaver;

public enum DataType {
    HEADER("Headers", "Headers"),
    BODY("Body", "Bodies");

    private final String _singularNoun;
    private final String _pluralNoun;

    DataType(String singularNoun, String pluralNoun) {
        _singularNoun = singularNoun;
        _pluralNoun = pluralNoun;
    }

    public String getNoun(boolean plural) {
        return plural ? _pluralNoun : _singularNoun;
    }
}
