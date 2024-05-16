import classNames from "classnames/bind";
import styles from "./Temperature.module.scss";

import {FaTemperatureHigh} from "react-icons/fa";

const cx = classNames.bind(styles);

function Item({temp}) {

    return (
        <div className={cx(temp <= 28 ? 'container_item-cloud' : (temp >= 34 ? 'container_item-hot' : 'container_item-sun'))}>
            <div className={cx('description')}>
                <p className={cx('title')}>Nhiệt độ</p>
                <p className={cx('temp')}>{temp}°C</p>
                </div>
            <div className={cx('img-des')}>
                <FaTemperatureHigh className={cx('icon')}/>
            </div>
        </div>
    );
}

export default Item;