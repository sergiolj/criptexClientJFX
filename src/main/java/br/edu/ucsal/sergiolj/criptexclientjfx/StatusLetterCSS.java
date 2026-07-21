package br.edu.ucsal.sergiolj.criptexclientjfx;

public enum StatusLetterCSS {
    EMPTY ("empty"),
    RIGHT ("right"),
    WRONG_PLACE ("wrong_place"),
    NON_EXISTENT ("non_existent");

    private final String cssClass;


    private StatusLetterCSS(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }
}
