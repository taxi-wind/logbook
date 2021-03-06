package logbook.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dyuproject.protostuff.Tag;

/**
 * 艦隊のドックを表します
 *
 */
public final class DockDto extends AbstractDto {

    /** ドックID */
    @Tag(1)
    private final String id;

    /** 艦隊名 */
    @Tag(2)
    private final String name;

    /** 艦娘達 */
    @Tag(3)
    private final List<ShipDto> ships = new ArrayList<ShipDto>();

    @Tag(4)
    private boolean[] escaped = null;

    /** 更新フラグ */
    private transient boolean update;

    /**
     * コンストラクター
     */
    public DockDto(String id, String name, DockDto oldDock) {
        this.id = id;
        this.name = name;
        if (oldDock != null) {
            this.escaped = oldDock.getEscaped();
        }
    }

    /**
     * ドックIDを取得します。
     * @return ドックID
     */
    public String getId() {
        return this.id;
    }

    /**
     * 艦娘を艦隊に追加します
     * 艦娘の艦隊所属情報も更新します
     * @param ship
     */
    public void addShip(ShipDto ship) {
        this.ships.add(ship);
    }

    /**
     * 艦隊から艦娘を削除します
     * @param ship
     */
    public void removeShip(ShipDto ship) {
        int index = this.ships.indexOf(ship);
        if (index != -1) {
            this.ships.remove(index);
        }
    }

    /**
     * 艦隊の艦娘を入れ替えます
     * @param index
     * @param newShip
     */
    public void setShip(int index, ShipDto newShip) {
        this.ships.set(index, newShip);
    }

    /**
     * 旗艦以外を外します
     */
    public void removeExceptFlagship() {
        while (this.ships.size() > 1) {
            this.ships.remove(this.ships.size() - 1);
        }
    }

    /**
     * 艦隊名を取得します。
     * @return 艦隊名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 艦娘達を取得します。
     * @return 艦娘達
     */
    public List<ShipDto> getShips() {
        return Collections.unmodifiableList(this.ships);
    }

    /**
     * 更新フラグを取得します。
     * @return 更新フラグ
     */
    public boolean isUpdate() {
        return this.update;
    }

    /**
     * 更新フラグを設定します。
     * @param update 更新フラグ
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }

    public void removeFleetIdFromShips() {
        for (int i = 0; i < this.ships.size(); i++) {
            this.ships.get(i).setFleetid("");
        }
    }

    public void updateFleetIdOfShips() {
        for (int i = 0; i < this.ships.size(); i++) {
            this.ships.get(i).setFleetid(this.id);
            this.ships.get(i).setFleetpos(i);
        }
    }

    /**
     * 大破艦がいるか？を取得します
     * @return 大破艦がいるか？
     */
    public boolean isBadlyDamaged() {
        for (int i = 0; i < this.ships.size(); ++i) {
            if ((this.escaped != null) && this.escaped[i]) {
                continue; // 退避した艦はカウントしない
            }
            ShipDto ship = this.ships.get(i);
            if (ship.isBadlyDamage()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return escaped
     */
    public boolean[] getEscaped() {
        return this.escaped;
    }

    /**
     * @param escaped セットする escaped
     */
    public void setEscaped(boolean[] escaped) {
        this.escaped = escaped;
    }
}
