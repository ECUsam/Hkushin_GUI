package GUI.Panel;


import GUI.UI.CustomList;


public class EventList<E> extends CustomList<E> {
    public EventListModel model;
    public EventList(){
        super();
        model = new EventListModel();
        this.setModel(model);
    }
}
