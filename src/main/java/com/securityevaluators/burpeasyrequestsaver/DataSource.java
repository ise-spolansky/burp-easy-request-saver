package com.securityevaluators.burpeasyrequestsaver;

public enum DataSource {
    REQUEST(),
    RESPONSE();

    private String _noun;

    DataSource() {
        _noun = name().substring(0,1).toUpperCase() + name().substring(1).toLowerCase();
    }
    DataSource(String noun) {
        _noun = noun;
    }

    public String getNoun() {
        return _noun;
    }
}