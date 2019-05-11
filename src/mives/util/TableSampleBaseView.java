/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.util;

import java.util.EnumSet;
import java.util.Iterator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import mives.model.MapaConfiguracao;

/**
 *
 * @author Ricardo
 */
public class TableSampleBaseView {

    public TableSampleBaseView() {

    }

    int tipoDeRegra;
    MapaConfiguracao configuracao;
    String labelRegra;

//    final ArrayList<Regra> regraAlteradas = new ArrayList<>();
    private int quantidade = 0;

    public TableSampleBaseView(int tipoDeRegra, MapaConfiguracao configuracao, String labelRegra) {
        this.tipoDeRegra = tipoDeRegra;
        this.configuracao = configuracao;
        this.labelRegra = labelRegra;
        this.carregarRegras();
    }
    public TableView<Regra> table = new TableView<>();
    ObservableList<Regra> data = FXCollections.observableArrayList();

    private void carregarRegras() {
//        System.out.println("Tipo Regra valor: " + tipoDeRegra);
        if (tipoDeRegra == 1) {
            //  System.out.println("Carregando as elisões - QTD: " + configuracao.getElisao().size());
            for (Iterator<String> iter = configuracao.getElisao().iterator();
                    iter.hasNext();) {
                String regraAtual = iter.next();
                if (configuracao.getExcecoesElisao().contains(regraAtual)) {
                    data.add(new Regra(1, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), Participation.TENTE));
                } else {
                    data.add(new Regra(1, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), Participation.SEMPRE));
                }
                quantidade++;
            }
            if (configuracao.getExcluirElisao() != null) {
//                System.out.println("NÃO ESTÁ NULO");
                for (Iterator<String> iter = configuracao.getExcluirElisao().iterator();
                        iter.hasNext();) {
                    String regraAtual = iter.next();
                    data.add(new Regra(1, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), Participation.NUNCA));
                }
            }
        } else if (tipoDeRegra == 2) {
            //  System.out.println("Carregando as Sinérese - QTD: " + configuracao.getSinerese().size());
            for (Iterator<String> iter = configuracao.getSinerese().iterator();
                    iter.hasNext();) {
                String regraAtual = iter.next();
                if (configuracao.getExcecoesSinerese().contains(regraAtual)) {
                    data.add(new Regra(2, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), Participation.TENTE));
                } else {
                    data.add(new Regra(2, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), Participation.SEMPRE));
                }
                quantidade++;
            }
            if (configuracao.getExcluirSinerese() != null) {
                for (Iterator<String> iter = configuracao.getExcluirSinerese().iterator(); iter.hasNext();) {
                    String regraAtual = iter.next();
                    data.add(new Regra(1, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), Participation.NUNCA));
                }
            }

        } else {

            for (Iterator<String> iter = configuracao.getDierese().iterator();
                    iter.hasNext();) {
                String regraAtual = iter.next();
                if (configuracao.getExcecoesDierese().contains(regraAtual)) {
                    data.add(new Regra(3, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), Participation.TENTE));
                } else {
                    data.add(new Regra(3, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), Participation.SEMPRE));
                }
                quantidade++;
            }
            if (configuracao.getExcluirDierese() != null) {
                for (Iterator<String> iter = configuracao.getExcluirDierese().iterator(); iter.hasNext();) {
                    String regraAtual = iter.next();
                    data.add(new Regra(1, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), Participation.NUNCA));
                }
            }

        }
    }

    public final HBox hb = new HBox();
    public Label label;
    Label labelQuantidade;

    public void montarTabela() {

        label = new Label(labelRegra);
        label.setFont(new Font("Arial", 12));

        TableColumn<Regra, String> firstNameCol = new TableColumn("LETRA 1");
        firstNameCol.setMinWidth(50);
        firstNameCol.setStyle("-fx-alignment: CENTER;");
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<>("primeiraLetra"));

        TableColumn<Regra, String> lastNameCol = new TableColumn("LETRA 2");
        lastNameCol.setMinWidth(50);
        lastNameCol.setStyle("-fx-alignment: CENTER;");
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<>("segundaLetra"));

        TableColumn<Regra, Participation> participationColumn = new TableColumn("PARTICIPAÇÃO");
        participationColumn.setCellFactory((param) -> new RadioButtonCell<Regra, Participation>(EnumSet.allOf(Participation.class)));
        participationColumn.setCellValueFactory(new PropertyValueFactory<Regra, Participation>("participation"));
        participationColumn.setOnEditCommit(
                new EventHandler<CellEditEvent<Regra, Participation>>() {
            Regra r1;

            @Override
            public void handle(CellEditEvent<Regra, Participation> t) {
                (r1 = (Regra) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setParticipation(t.getNewValue());
                System.out.println("Regra alterada: " + r1.getPrimeiraLetra() + "- " + r1.getSegundaLetra());
            }
        }
        );

        table.getStylesheets().add(getClass().getResource("table.css").toExternalForm());

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, participationColumn);

//        final TextField primeiraLetra = new TextField();
        final MaskTextField primeiraLetra = new MaskTextField();
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Por favor, entre com uma vogal.");
        primeiraLetra.setTooltip(tooltip);
        primeiraLetra.setMask("*");
        primeiraLetra.setPromptText("Primeira Letra");
        primeiraLetra.setMaxWidth(firstNameCol.getPrefWidth());

//        final TextField segundaLetra = new TextField();
        final MaskTextField segundaLetra = new MaskTextField();
        segundaLetra.setTooltip(tooltip);
        segundaLetra.setMask("*");
        segundaLetra.setMaxWidth(lastNameCol.getPrefWidth());
        segundaLetra.setPromptText("Segunda Letra");

        final Button addButton = new Button("Adicionar");
        addButton.getStylesheets().add(getClass().getResource("botaoNavegacao.css").toExternalForm());

        addButton.setOnAction((ActionEvent e) -> {
            System.out.println("Peguei: " + (primeiraLetra.getText()).toLowerCase());
            if (!(Utilitario.isVogalInterface((primeiraLetra.getText()).toLowerCase().trim()))) {
                alertaDeVogal();
                primeiraLetra.requestFocus();
                return;
            }
            if (!(Utilitario.isVogalInterface((segundaLetra.getText()).toLowerCase()))) {
                alertaDeVogal();
                segundaLetra.requestFocus();
                return;
            }
            data.add(new Regra(1,
                    (primeiraLetra.getText()).toUpperCase(),
                    (segundaLetra.getText()).toUpperCase(),
                    //   addEmail.getText(),
                    Participation.TENTE
            ));
            primeiraLetra.clear();
            segundaLetra.clear();
            // addEmail.clear();
        });

        final Button removeButton = new Button("Remover");
        removeButton.getStylesheets().add(getClass().getResource("botaoNavegacao.css").toExternalForm());
//        removeButton.setDisable(true);
        removeButton.setOnAction((ActionEvent e) -> {
            Regra r1 = table.getSelectionModel().getSelectedItem();
            if (r1 == null) {
                Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
                dialogoInfo.setTitle("Modificar Parâmetros de Escansão.");
                dialogoInfo.setHeaderText("Não é possível executar a ação selecionada!");
                dialogoInfo.setContentText("Para excluir um parâmetro é necessário escolher\n o item que se deseja excluir na listagem.");
                dialogoInfo.showAndWait();

                //System.out.println("Nenhuma linha está selecionada");
                return;
            }
            if (perguntarAoUsuario(r1.toString())) {
                if (r1.getTipoDeRegra() == 1) {//Elisão
                    if (MapaConfiguracao.getInstacia().getElisao().contains(r1.toString())) {
                        MapaConfiguracao.getInstacia().getElisao().remove(r1.toString());
                    }
                    if (MapaConfiguracao.getInstacia().getExcecoesElisao().contains(r1.toString())) {
                        MapaConfiguracao.getInstacia().getExcecoesElisao().remove(r1.toString());
                    }
                    if (MapaConfiguracao.getInstacia().getExcluirElisao().contains(r1.toString())) {
                        MapaConfiguracao.getInstacia().getExcluirElisao().remove(r1.toString());
                    }

                } else if (r1.getTipoDeRegra() == 2) {//Sinerese
                    if (MapaConfiguracao.getInstacia().getSinerese().contains(r1.toString())) {
                        MapaConfiguracao.getInstacia().getSinerese().remove(r1.toString());
                    }
                    if (MapaConfiguracao.getInstacia().getExcecoesSinerese().contains(r1.toString())) {
                        MapaConfiguracao.getInstacia().getExcecoesSinerese().remove(r1.toString());
                    }
                    if (MapaConfiguracao.getInstacia().getExcluirSinerese().contains(r1.toString())) {
                        MapaConfiguracao.getInstacia().getExcluirSinerese().remove(r1.toString());
                    }

                } else {//Dierese
                    if (MapaConfiguracao.getInstacia().getDierese().contains(r1.toString())) {
                        MapaConfiguracao.getInstacia().getDierese().remove(r1.toString());
                    }
                    if (MapaConfiguracao.getInstacia().getExcecoesDierese().contains(r1.toString())) {
                        MapaConfiguracao.getInstacia().getExcecoesDierese().remove(r1.toString());
                    }
                    if (MapaConfiguracao.getInstacia().getExcluirDierese().contains(r1.toString())) {
                        MapaConfiguracao.getInstacia().getExcluirDierese().remove(r1.toString());
                    }
                }

                data.remove(r1);
            }
            removeButton.setDisable(true);

        });

        table.setTableMenuButtonVisible(true);

        table.setRowFactory(tv -> {
            TableRow<Regra> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                Regra regra = row.getItem();
                System.out.println("row.getItem()" + row.getItem());
                if (regra != null) {
                    removeButton.setDisable(false);
                }
            });
            return row;
        });

        // table.setMinWidth(280);
        //  table.setMaxHeight(230);
        labelQuantidade = new Label("Total: " + quantidade);
//        hb.getChildren().addAll(primeiraLetra, segundaLetra, addButton, removeButton, labelQuantidade);
        hb.getChildren().addAll(primeiraLetra, segundaLetra, addButton, removeButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);
    }

    Boolean resposta = new Boolean(true);

    private void alertaDeVogal() {
        Alert dialogoInfo = new Alert(Alert.AlertType.WARNING);
        dialogoInfo.setTitle("Modificar Parâmetros de Escansão.");
        dialogoInfo.setHeaderText("Não é possível executar a ação selecionada!");
        dialogoInfo.setContentText("Para adicionar um novo parâmetro é necessário entrar com um par de vogais.");
        dialogoInfo.showAndWait();
    }

    private boolean perguntarAoUsuario(String regra) {
        Alert dialogoExe = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType btnSim = new ButtonType("Sim");
        ButtonType btnNao = new ButtonType("Não");
        resposta = false;
        ButtonType btnNaoResponder = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialogoExe.setTitle("Modificar Parâmetros de Escansão.");
        dialogoExe.setHeaderText("Tem certeza que deseja excluir a regra selecionada?");
        dialogoExe.setContentText(regra.toUpperCase());

        dialogoExe.getButtonTypes().setAll(btnSim, btnNao, btnNaoResponder);

        dialogoExe.showAndWait().ifPresent(b -> {
            if (b == btnSim) {
                resposta = true;
            } else if (b == btnNao) {
                resposta = false;
            }
        });

        return resposta;
    }

    public static enum Participation {
        SEMPRE,
        NUNCA,
        TENTE;

        public String toString() {
            return super.toString().toLowerCase();
        }
    ;

    }
    
    public class RadioButtonCell<S, T extends Enum<T>> extends TableCell<S, T> {

        private EnumSet<T> enumeration;

        public RadioButtonCell(EnumSet<T> enumeration) {
            this.enumeration = enumeration;
        }

        @Override
        protected void updateItem(T item, boolean empty) {

            super.updateItem(item, empty);
            if (!empty) {
                // gui setup
                HBox hb = new HBox(7);
                hb.setAlignment(Pos.CENTER);
                final ToggleGroup group = new ToggleGroup();

                // create a radio button for each 'element' of the enumeration
                for (Enum<T> enumElement : enumeration) {
                    RadioButton radioButton = new RadioButton(enumElement.toString());
                    radioButton.setUserData(enumElement);
                    radioButton.setToggleGroup(group);
                    hb.getChildren().add(radioButton);
                    if (enumElement.equals(item)) {
                        radioButton.setSelected(true);
                    }
                }

                // issue events on change of the selected radio button
                group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void changed(ObservableValue<? extends Toggle> observable,
                            Toggle oldValue, Toggle newValue) {
                        getTableView().edit(getIndex(), getTableColumn());
                        RadioButtonCell.this.commitEdit((T) newValue.getUserData());
                        // System.out.println("Linha: " + getIndex() + "\n" + "Novo valor: " + newValue.getUserData());
                        getTableView().getSelectionModel().select(getIndex());
                        Regra r1 = (Regra) getTableView().getSelectionModel().getSelectedItem();
                        System.out.println("------------------------------------");
                        System.out.println("Tipo de Regra: " + r1.getTipoDeRegra());
                        System.out.println("Regra selecionada: " + r1.getPrimeiraLetra() + r1.getSegundaLetra());
                        System.out.println("Opção: " + newValue.getUserData());

//                        if (!(newValue.getUserData().toString().equals(oldValue.getUserData().toString()))) {
//                            if (r1.getTipoDeRegra() == 1) {//Elisão
//                                tratarElisao(newValue.getUserData().toString(), r1);
//                            } else if (r1.getTipoDeRegra() == 2) {//Sinérese
//                                tratarSinerese(newValue.getUserData().toString(), r1);
//                            } else {//Dierese
//                                tratarDierese(newValue.getUserData().toString(), r1);
//                            }
//                        }
                        if (!(newValue.getUserData().toString().equals(oldValue.getUserData().toString()))) {
                            if (r1.getTipoDeRegra() == 1) {//Elisão
                                tratarElisao(oldValue.getUserData().toString(), newValue.getUserData().toString(), r1);
                            } else if (r1.getTipoDeRegra() == 2) {//Sinérese
                                tratarSinerese(oldValue.getUserData().toString(), newValue.getUserData().toString(), r1);
                            } else {//Dierese
                                tratarDierese(oldValue.getUserData().toString(), newValue.getUserData().toString(), r1);
                            }
                        }

                    }
                });

                setGraphic(hb);
            }
        }

        @Deprecated
        private void tratarElisao(String newValue, Regra r1) {
            System.out.println("Tratando Elisão");
            if (newValue.equals("sim")) {
                System.out.println("Removendo da lista de excecões: " + r1.toString());
                MapaConfiguracao.getInstacia().getExcecoesElisao().remove(r1.toString());
            } else {
                System.out.println("Adicionando na lista de excecões");
                MapaConfiguracao.getInstacia().getExcecoesElisao().add(r1.toString());
            }
        }

        private void tratarElisao(String oldValue, String newValue, Regra r1) {
            System.out.println("Tratando Elisão");
            if (newValue.equals("sempre")) {
                if (oldValue.equals("tente")) {
                    MapaConfiguracao.getInstacia().getExcecoesElisao().remove(r1.toString());
                } else {
                    if (oldValue.equals("nunca")) {

                        MapaConfiguracao.getInstacia().getExcluirElisao().remove(r1.toString());
                        MapaConfiguracao.getInstacia().getElisao().add(r1.toString());
                    }
                }
                MapaConfiguracao.getInstacia().getExcecoesElisao().remove(r1.toString());
            } else if (newValue.equals("tente")) {
                if (oldValue.equals("sempre")) {
                    MapaConfiguracao.getInstacia().getExcecoesElisao().add(r1.toString());
                } else {
                    if (oldValue.equals("nunca")) {
                        MapaConfiguracao.getInstacia().getExcluirElisao().remove(r1.toString());
                        MapaConfiguracao.getInstacia().getElisao().add(r1.toString());
                        MapaConfiguracao.getInstacia().getExcecoesElisao().add(r1.toString());
                    }
                }
            } else {
                MapaConfiguracao.getInstacia().getExcluirElisao().add(r1.toString());
                if (MapaConfiguracao.getInstacia().getElisao().contains(r1.toString())) {
                    MapaConfiguracao.getInstacia().getElisao().remove(r1.toString());
                }
                if (MapaConfiguracao.getInstacia().getExcecoesElisao().contains(r1.toString())) {
                    MapaConfiguracao.getInstacia().getExcecoesElisao().remove(r1.toString());
                }
            }
        }

        @Deprecated
        private void tratarSinerese(String newValue, Regra r1) {
            if (newValue.equals("sim")) {
                MapaConfiguracao.getInstacia().getExcecoesSinerese().remove(r1.toString());
            } else {
                MapaConfiguracao.getInstacia().getExcecoesSinerese().add(r1.toString());
            }
        }

        private void tratarSinerese(String oldValue, String newValue, Regra r1) {
            System.out.println("Tratando Sinérese");
            if (newValue.equals("sempre")) {
                if (oldValue.equals("tente")) {
                    MapaConfiguracao.getInstacia().getExcecoesSinerese().remove(r1.toString());
                } else {
                    if (oldValue.equals("nunca")) {
                        MapaConfiguracao.getInstacia().getExcluirSinerese().remove(r1.toString());
                        MapaConfiguracao.getInstacia().getSinerese().add(r1.toString());
                    }
                }
                MapaConfiguracao.getInstacia().getExcecoesSinerese().remove(r1.toString());
            } else if (newValue.equals("tente")) {
                if (oldValue.equals("sempre")) {
                    MapaConfiguracao.getInstacia().getExcecoesSinerese().add(r1.toString());
                } else {
                    if (oldValue.equals("nunca")) {
                        MapaConfiguracao.getInstacia().getExcluirSinerese().remove(r1.toString());
                        MapaConfiguracao.getInstacia().getSinerese().add(r1.toString());
                        MapaConfiguracao.getInstacia().getExcecoesSinerese().add(r1.toString());
                    }
                }
            } else {
                MapaConfiguracao.getInstacia().getExcluirSinerese().add(r1.toString());
                if (MapaConfiguracao.getInstacia().getSinerese().contains(r1.toString())) {
                    MapaConfiguracao.getInstacia().getSinerese().remove(r1.toString());
                }
                if (MapaConfiguracao.getInstacia().getExcecoesSinerese().contains(r1.toString())) {
                    MapaConfiguracao.getInstacia().getExcecoesSinerese().remove(r1.toString());
                }
            }
        }

        @Deprecated
        private void tratarDierese(String newValue, Regra r1) {
            if (newValue.equals("sim")) {
                MapaConfiguracao.getInstacia().getExcecoesDierese().remove(r1.toString());
            } else {
                MapaConfiguracao.getInstacia().getExcecoesDierese().add(r1.toString());
            }
        }

        private void tratarDierese(String oldValue, String newValue, Regra r1) {
            System.out.println("Tratando Diérese");
            if (newValue.equals("sempre")) {
                if (oldValue.equals("tente")) {
                    MapaConfiguracao.getInstacia().getExcecoesDierese().remove(r1.toString());
                } else {
                    if (oldValue.equals("nunca")) {
                        MapaConfiguracao.getInstacia().getExcluirDierese().remove(r1.toString());
                        MapaConfiguracao.getInstacia().getDierese().add(r1.toString());
                    }
                }
                MapaConfiguracao.getInstacia().getExcecoesDierese().remove(r1.toString());
            } else if (newValue.equals("tente")) {
                if (oldValue.equals("sempre")) {
                    MapaConfiguracao.getInstacia().getExcecoesDierese().add(r1.toString());
                } else {
                    if (oldValue.equals("nunca")) {
                        MapaConfiguracao.getInstacia().getExcluirDierese().remove(r1.toString());
                        MapaConfiguracao.getInstacia().getDierese().add(r1.toString());
                        MapaConfiguracao.getInstacia().getExcecoesDierese().add(r1.toString());
                    }
                }
            } else {
                MapaConfiguracao.getInstacia().getExcluirDierese().add(r1.toString());
                if (MapaConfiguracao.getInstacia().getDierese().contains(r1.toString())) {
                    MapaConfiguracao.getInstacia().getDierese().remove(r1.toString());
                }
                if (MapaConfiguracao.getInstacia().getExcecoesDierese().contains(r1.toString())) {
                    MapaConfiguracao.getInstacia().getExcecoesDierese().remove(r1.toString());
                }
            }
        }

    }

}
