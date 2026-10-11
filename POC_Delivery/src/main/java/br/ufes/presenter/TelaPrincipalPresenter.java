
package br.ufes.presenter;

import br.ufes.view.TelaPrincipalView;

public class TelaPrincipalPresenter {
    
    private TelaPrincipalView view;
    
    public TelaPrincipalPresenter(){
    view = new TelaPrincipalView();
    
    configuraView();
    
    }
    
    
    
    public void configuraView(){
        view.setVisible(true);
    }
}
