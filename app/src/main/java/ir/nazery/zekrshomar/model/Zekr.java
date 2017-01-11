package ir.nazery.zekrshomar.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/***
 * Created by REZA on 25/1/2016
 ***/

@Table(name = "Zekrs")
public class Zekr extends Model {

    private static final String ZEKR_NAME = "zekr_name";
    private static final String ZEKR_COUNT = "zekr_count";
    private static final String POSITION = "position";

    @Column(name = ZEKR_NAME)
    public String zekrName;

    @Column(name = ZEKR_COUNT)
    public Integer zekrCount;

    @Column(name = POSITION)
    public Integer position;


//    public Zekr(Bundle bundle) {
//        super();
//
//        if (bundle != null) {
//            this.zekrName = bundle.getString(ZEKR_NAME, "");
//            this.zekrCount = bundle.getInt(ZEKR_COUNT, 0);
//            this.position = bundle.getInt(POSITION, -1);
//        } else {
//            this.zekrName = "";
//            this.zekrCount = 0;
////            this.position = -1;
//        }
//    }

    public Zekr(String zekrName, int zekrCount, int position) {
        super();

        this.zekrName = zekrName;
        this.zekrCount = zekrCount;
        this.position = position;
    }

    public Zekr() {
        super();

        this.zekrName = "";
        this.zekrCount = 0;
//        this.position = -1;
    }

    public Zekr(Zekr zekr) {
        super();

        this.zekrName = zekr.getZekrName();
        this.zekrCount = zekr.getZekrCountAsInteger();
        this.position = zekr.getPosition();
    }

    public static List<Zekr> getAll() {
        return new Select()
                .from(Zekr.class)
                .orderBy(POSITION)
                .execute();
    }

//    public static void update(Zekr zekr) {
//        new Update(Zekr.class)
//                .set(String.format("%s = ?, %s = ?, %s = ?", ZEKR_NAME, ZEKR_COUNT, POSITION),
//                        zekr.getZekrName(), zekr.getZekrCountAsInteger(), zekr.position)
//                .where("id = ?", zekr.getId())
//                .execute();
//    }

//    public Bundle toBundle() {
//        Bundle b = new Bundle();
//        b.putString(ZEKR_NAME, getZekrName());
//        b.putInt(ZEKR_COUNT, getZekrCountAsInteger());
//        b.putInt(POSITION, position);
//        return b;
//    }

    public String getZekrName() {
        return zekrName;
    }

    public void setZekrName(String zekrName) {
        this.zekrName = zekrName;
    }

    public Integer getZekrCountAsInteger() {
        return zekrCount;
    }

    public String getZekrCountAsString() {
        return zekrCount.toString();
    }

    public void setZekrCount(int zekrCount) {
        this.zekrCount = zekrCount;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
