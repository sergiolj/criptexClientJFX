package ui.component;

/**
 * Componente de interface para nomenclatura de classes a serem aplicadas para estilização de elementos gráficos.
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
public enum StatusLetter {
    EMPTY ("empty"),
    RIGHT ("right"),
    WRONG_PLACE ("wrong_place"),
    NON_EXISTENT ("non_existent");

    private final String cssClass;


    private StatusLetter(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }
}
