package utilities.generaltrade;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Implements {
	 // =========================================================================
 	 // TODO Inner Class
 	 // =========================================================================
	public static class SelectEstablismentCheckList implements OnItemClickListener{

		private ListView lvNearEstablishment;
		
		public SelectEstablismentCheckList(ListView lvNearEstablishment){
			this.lvNearEstablishment = lvNearEstablishment;
		}
		
		@Override
		public void onItemClick(AdapterView<?> av, View view, int pos, long poslong) {
			
			switch (pos) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:	
				if (Utilities.checkEstablishment(lvNearEstablishment) == false) {
					// If all Items 1-5 is unchecked
					lvNearEstablishment.setItemChecked(7, true);
				} else {
					// Unchecked "None" Item
					lvNearEstablishment.setItemChecked(7, false);
				}
				break;

			default:
				lvNearEstablishment.setItemChecked(0, false);
				lvNearEstablishment.setItemChecked(1, false);
				lvNearEstablishment.setItemChecked(2, false);
				lvNearEstablishment.setItemChecked(3, false);
				lvNearEstablishment.setItemChecked(4, false);
				lvNearEstablishment.setItemChecked(5, false);
				lvNearEstablishment.setItemChecked(6, false);
				lvNearEstablishment.setItemChecked(7, true);
			}
		}
		 // =========================================================================
	 	 // TODO Inner Class
	 	 // =========================================================================
	}
}
