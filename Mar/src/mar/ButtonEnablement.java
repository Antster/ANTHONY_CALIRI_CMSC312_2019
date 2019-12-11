/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mar;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class ButtonEnablement implements DocumentListener {

        private ButtonModel buttonModel;
        private List<Document> documents = new ArrayList<Document>();

        public ButtonEnablement(ButtonModel buttonModel) {
            this.buttonModel = buttonModel;
        }

        public void addDocument(Document document) {
            document.addDocumentListener(this);
            this.documents.add(document);
            documentChanged();
        }

        public void documentChanged() {
            boolean buttonEnabled = false;
            for (Document document : documents) {
                try {
                    if (document.getLength() > 0 && document.getText(0, document.getLength()).matches("[0-9]+")) {
                        buttonEnabled = true;
                        break;
                    }
                } catch (BadLocationException ex) {
                    Logger.getLogger(ButtonEnablement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            buttonModel.setEnabled(buttonEnabled);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            documentChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            documentChanged();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            documentChanged();
        }
    }