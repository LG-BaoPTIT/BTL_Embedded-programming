import classNames from "classnames/bind";
import styles from "./Home.module.scss";
import axios from "axios";
import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import React from "react";
import Tippy from "@tippyjs/react/headless";
import { w3cwebsocket as W3CWebSocket } from "websocket";

import Temperature from "../../components/temperature/Temperature";
import Humidity from "../../components/humidity/Humidity";
import DustLevel from "../../components/dustLevel/DustLevel";
import AreaChart from "../../components/chart/DataSensorChart/AreaChart";
import GasChart from "../../components/chart/DustLevelChart/DustChart";
import Nav from "../../components/navbar/Nav";
// import WebSocket from "../../websocket/WebSoket";

import ImgLight from "../../img/idea.png";
import LightOf from "../../img/big-light.png";
import DoorClose from '../../img/doorclose.png'
import DoorOpen from '../../img/dooropen.png'
import { HiBars3 } from "react-icons/hi2";
import { StompSessionProvider, useSubscription } from "react-stomp-hooks";
const cx = classNames.bind(styles);

export default function DashBoard() {
    return (
        //Initialize Stomp connection, will use SockJS for http(s) and WebSocket for ws(s)
        //The Connection can be used by all child components via the hooks or hocs.
        <StompSessionProvider
            url={"ws://localhost:8080/ws"}
            //All options supported by @stomp/stompjs can be used here
        >
            <Home />
        </StompSessionProvider>
    );
}

function Home() {
    const location = useLocation();
    const props = location.state;
    const [light1, setLight1] = useState(
        props ? props.stateLed : false
    );
    const [light2, setLight2] = useState(
        props ? props.stateLed : false
    );
    const [light3, setLight3] = useState(
        props ? props.stateLed : false
    );
    const [door, setDoor] = useState(
        props ? props.stateFan : false
    );
    const [dataSensor, setDataSensor] = useState(null);
    const [gas, setGas] = useState(null);

    useSubscription("/topic/DHT11_data/SMH-001/DHT11_S", (message) =>
        setDataSensor(JSON.parse(message.body))
    );
    useSubscription("/topic/gas_data/SMH-001/GAS_SENSOR_S", (message) =>
        setGas(JSON.parse(message.body))
    );
    useSubscription('/topic/light_data/SMH-001/PB_001', message => {
        setLight1(JSON.parse(message.body)?.status === 'ON')
    })
    useSubscription('/topic/light_data/SMH-001/PK_001', message => {
        setLight2(JSON.parse(message.body)?.status === 'ON')
    })
    useSubscription('/topic/light_data/SMH-001/PN_001', message => {
        setLight3(JSON.parse(message.body)?.status === 'ON')
    })
    useSubscription('/topic/door_data/SMH-001/MAIN_DOOR', message => {
        setDoor(JSON.parse(message.body)?.status === 'Opened')
    })

    const getFormattedTimestamp = () => {
        const date = new Date();
        const year = date.getFullYear();
        let month = String(date.getMonth() + 1).padStart(2, "0");
        let day = String(date.getDate()).padStart(2, "0");
        let hour = String(date.getHours()).padStart(2, "0");
        let minute = String(date.getMinutes()).padStart(2, "0");
        let second = String(date.getSeconds()).padStart(2, "0");
        return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
    };

    const renderTippy = (prop) => {
        return (
            <div>
                <Nav
                    props={{
                        stateLed1: light1,
                        stateLead2: light2,
                        stateLed3: light3,
                        stateFan: door,
                    }}
                />
            </div>
        );
    };

    const handleClick = (deviceType, lightNum) => {
        let status;
        let light_id;
        const home_id = localStorage.getItem("home_id");

        const time = getFormattedTimestamp();
        if (deviceType === "Light") {
            if (lightNum === 1) {
                light_id = "PB_001";
                status = light1 ? "OFF" : "ON";
                setLight1((prev) => !prev);
            }
            if (lightNum === 2) {
                light_id = "PK_001";
                status = light2 ? "OFF" : "ON";
                setLight2((prev) => !prev);
            }
            if (lightNum === 3) {
                light_id = "PN_001";
                status = light3 ? "OFF" : "ON";
                setLight3((prev) => !prev);
            }

            axios
                .post("http://localhost:8080/changeLightStatus", {
                    home_id,
                    light_id,
                    status,
                })
                .then((res) => console.log(res))
                .catch((e) => console.log(e));
        } else {
            status = door ? "Closed" : "Opened";
            setDoor((prev) => !prev);

            axios
                .post("http://localhost:8080/changeDoorStatus", {
                    home_id,
                    door_id: "MAIN_DOOR",
                    status,
                })
                .then((res) => console.log(res))
                .catch((e) => console.log(e));
        }
    };
    return (
        <div className={cx("container_app")}>
            <div>
                <div className={cx("title")}>
                    <h3>IoT & Ứng dụng</h3>
                    <Tippy
                        render={renderTippy}
                        interactive
                        delay={[100, 100]}
                        offset={[-85, -3]}
                        placement="bottom"
                    >
                        <span className={cx("icon-nav")}>
                            <HiBars3 />
                        </span>
                    </Tippy>
                </div>
            </div>
            <div className={cx("container_app-header")}>
                <div className={cx("row")}>
                    <div className={cx("col-4")}>
                        <Temperature
                            temp={
                                dataSensor
                                    ? dataSensor.temperature.toFixed(1)
                                    : ""
                            }
                        />
                    </div>
                    <div className={cx("col-4")}>
                        <Humidity
                            humidity={
                                dataSensor ? dataSensor.humidity.toFixed(1) : ""
                            }
                        />
                    </div>
                    <div className={cx("col-4")}>
                        <DustLevel
                            gas={gas}
                        />
                    </div>
                </div>
            </div>
            <div className={cx("container_app-body")}>
                <div className="row">
                    <div className="col-3">
                        <div className={cx("item-light")}>
                            {light1 ? (
                                <div className={cx("item")}>
                                    <img
                                        src={ImgLight}
                                        alt="Light On"
                                        className={cx("light-on")}
                                    />
                                    <button
                                        onClick={() => handleClick("Light", 1)}
                                        className={cx("off")}
                                    >
                                        OFF
                                    </button>
                                    <p className={cx('title')}>Phòng bếp</p>

                                </div>
                            ) : (
                                <div className={cx("item")}>
                                    <img
                                        src={LightOf}
                                        alt="Light Off"
                                        className={cx("light-off")}
                                    />
                                    <button
                                        onClick={() => handleClick("Light", 1)}
                                        className={cx("on")}
                                    >
                                        ON
                                    </button>
                                    <p className={cx('title')}>Phòng bếp</p>
                                </div>
                            )}
                        </div>
                    </div>
                    <div className="col-3">
                        <div className={cx("item-light")}>
                            {light2 ? (
                                <div className={cx("item")}>
                                    <img
                                        src={ImgLight}
                                        alt="Light On"
                                        className={cx("light-on")}
                                    />
                                    <button
                                        onClick={() => handleClick("Light", 2)}
                                        className={cx("off")}
                                    >
                                        OFF
                                    </button>
                                    <p className={cx('title')}>Phòng khách</p>
                                </div>
                            ) : (
                                <div className={cx("item")}>
                                    <img
                                        src={LightOf}
                                        alt="Light Off"
                                        className={cx("light-off")}
                                    />
                                    <button
                                        onClick={() => handleClick("Light", 2)}
                                        className={cx("on")}
                                    >
                                        ON
                                    </button>
                                    <p className={cx('title')}>Phòng khách</p>

                                </div>
                            )}
                        </div>
                    </div>
                    <div className="col-3">
                        <div className={cx("item-light")}>
                            {light3 ? (
                                <div className={cx("item")}>
                                    <img
                                        src={ImgLight}
                                        alt="Light On"
                                        className={cx("light-on")}
                                    />
                                    <button
                                        onClick={() => handleClick("Light", 3)}
                                        className={cx("off")}
                                    >
                                        OFF
                                    </button>
                                    <p className={cx('title')}>Phòng ngủ</p>
                                </div>
                            ) : (
                                <div className={cx("item")}>
                                    <img
                                        src={LightOf}
                                        alt="Light Off"
                                        className={cx("light-off")}
                                    />
                                    <button
                                        onClick={() => handleClick("Light", 3)}
                                        className={cx("on")}
                                    >
                                        ON
                                    </button>
                                    <p className={cx('title')}>Phòng ngủ</p>

                                </div>
                            )}
                        </div>
                    </div>
                    <div className="col-3">
                        <div
                            className={cx("item-fan")}
                            style={{ marginTop: 40 }}
                        >
                            {door ? (
                                <div className={cx("item")}>
                                    <img
                                        src={DoorOpen}
                                        alt="Fan On"
                                        className={cx("fan-on")}
                                    />
                                    <button
                                        onClick={() => handleClick("Fan")}
                                        className={cx("off")}
                                    >
                                        CLOSE
                                    </button>
                                    <p className={cx('title')}>Cửa chính</p>
                                </div>
                            ) : (
                                <div className={cx("item")}>
                                    <img
                                        src={DoorClose}
                                        alt="Fan Off"
                                        className={cx("fan-off")}
                                    />
                                    <button
                                        onClick={() => handleClick("Fan")}
                                        className={cx("on")}
                                    >
                                        OPEN
                                    </button>
                                    <p className={cx('title')}>Cửa chính</p>

                                </div>
                            )}
                        </div>
                    </div>
                </div>
                <AreaChart data={dataSensor ? dataSensor : ""} />
                <GasChart data = {gas}/>
            </div>
        </div>
    );
}
