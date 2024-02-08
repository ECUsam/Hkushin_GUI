package GUI.Panel;


import Constants.Constants;
import GUI.UI.CustomList;
import GUI.UI.EventListCellRender;


public class EventList extends CustomList<EventCellData> {
    public EventListModel model;
    public EventList(){
        super();
        model = new EventListModel();
        this.setModel(model);
        this.setCellRenderer(new EventListCellRender());
        this.setFont(Constants.font);
    }
}