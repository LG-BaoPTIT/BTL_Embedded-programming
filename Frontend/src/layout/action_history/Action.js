import classNames from "classnames/bind";
import styles from "./Styles.module.scss";
import { useState, useEffect } from "react";
import axios from "axios";
import { useLocation } from "react-router-dom";
import Tippy from "@tippyjs/react/headless";
import { HiBars3 } from "react-icons/hi2";
import { IoMdArrowDropdown } from "react-icons/io";

import Nav from "../../components/navbar/Nav";
import SelectBox from "../../components/selectbox/SelectBox";

const cx = classNames.bind(styles);

function History() {
    const location = useLocation();
    const props = location.state;
    const [data, setData] = useState([]);
    const [filterPage, setFilterPage] = useState([])
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
    const [type, setType] = useState('dht');
    const [keyword, setKeyword] = useState('');
    const [curPage, setCurPage] = useState(0)
    const [pageSize, setPageSize] = useState(0)

    const propsData = {
        startDate,
        endDate,
        type,
        keyword,
        setStartDate,
        setEndDate,
        setType,
        setKeyword,
    };


    const onSearch = async () => {
        startDate.setHours(0)
        startDate.setMinutes(0)
        startDate.setSeconds(0)

        const eDate = new Date()
        eDate.setFullYear(endDate.getFullYear())
        eDate.setMonth(endDate.getMonth())
        eDate.setDate(endDate.getDate() + 1)
        eDate.setHours(23)
        eDate.setMinutes(59)
        eDate.setSeconds(59)
        axios.post('http://localhost:8080/SearchLog', {
            type,
            start: startDate,
            end: eDate,
            keyWord: keyword
        }).then((res) => setData(res.data.sort((a, b) => {
            return new Date(b.timestamp) - new Date(a.timestamp)
        })))
    }

    useEffect(() => {
        setData([])
        setCurPage(0)
        setPageSize(0)
    }, [type])

    useEffect(() => {
        setPageSize(Math.min(Math.ceil((data?.length || 0) / 50), 25))
    }, [data])

    useEffect(() => {
        setFilterPage(data.slice(curPage * 50, Math.min((curPage + 1) * 50, data.length)))
    }, [curPage, data])

    const renderTippy = (prop) => {
        return (
            <div>
                <Nav props={props} />
            </div>
        );
    };

    return (
        <div className={cx("ctn")}>
            <div className={cx("container")}>
                <div className={cx("content")}>
                    <div className={cx("card")}>
                        <div className={cx("card-header")}>
                            <h4>Lịch sử</h4>
                            <Tippy
                                render={renderTippy}
                                interactive
                                delay={[200, 100]}
                                offset={[-85, 10]}
                                placement="bottom"
                            >
                                <span className={cx("icon-nav")}>
                                    <HiBars3 className={cx("icon")} />
                                </span>
                            </Tippy>
                        </div>

                        <div className={cx("card-body")}>
                            <SelectBox {...propsData} onSearch={onSearch} />
                            {data.length > 0 ? (
                                <>
                                    <table className={cx("table")}>
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                {type !== 'gas' && <th>Name</th>}
                                                <th>Date</th>
                                                {type === 'dht' && <>
                                                    <th>Humidity</th>
                                                    <th>Temperature</th>
                                                </>}
                                                {type === 'gas' && <th>Gas</th>}
                                                {type === 'door' && <>
                                                    <th>Status</th>
                                                    <th>User name</th>
                                                </>}
                                                {/* Add more table headers based on your data structure */}
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {filterPage.map((item, index) => {
                                                const timestampDate = new Date(item.timestamp);
                                                const formattedTimestamp = `${timestampDate.getFullYear()}-${(timestampDate.getMonth() + 1).toString().padStart(2, '0')}-${timestampDate.getDate().toString().padStart(2, '0')} ${timestampDate.getHours().toString().padStart(2, '0')}:${timestampDate.getMinutes().toString().padStart(2, '0')}:${timestampDate.getSeconds().toString().padStart(2, '0')}`;

                                                return (
                                                    <tr key={index} style={{ color: item?.value > 0 ? 'red' : '#fff' }}>
                                                        <td>{item.dhtId || item.gas_sensor_id || item.doorId}</td>
                                                        {type !== 'gas' && <td>{item.description}</td>}
                                                        <td>{formattedTimestamp}</td>
                                                        {type === 'dht' && (
                                                            <>
                                                                <td>{item.humidity?.toFixed(1)}</td>
                                                                <td>{item.temperature?.toFixed(1)}</td>
                                                            </>
                                                        )}
                                                        {type === 'gas' && <td>{item.value?.toFixed(1)}</td>}
                                                        {type === 'door' && (
                                                            <>
                                                                <td>{item.status}</td>
                                                                <td>{item.name}</td>
                                                            </>
                                                        )}
                                                        {/* Add more table data based on your data structure */}
                                                    </tr>
                                                );
                                            })}
                                        </tbody>

                                    </table>
                                    <nav aria-label="Page navigation example">
                                        <ul className="pagination">
                                            <li
                                                className="page-item">
                                                <button className="page-link"
                                                    disabled={curPage === 0}
                                                    style={{ backgroundColor: 'transparent', color: '#fff' }}
                                                    onClick={e => setCurPage(curPage - 1)}
                                                >
                                                    Prev
                                                </button>
                                            </li>
                                            {
                                                Array.from({
                                                    length: pageSize
                                                }, (_, index) => index)
                                                    .map(v =>
                                                        <li
                                                            key={v}
                                                            className="page-item">
                                                            <button className="page-link"
                                                                style={{ color: curPage === v ? '#000' : '#fff', backgroundColor: 'transparent' }}
                                                                onClick={e => setCurPage(v)}
                                                            >
                                                                {v + 1}
                                                            </button>
                                                        </li>
                                                    )
                                            }
                                            <li
                                                className="page-item">
                                                <button
                                                    className="page-link"
                                                    disabled={curPage === pageSize - 1}
                                                    style={{ backgroundColor: 'transparent', color: '#fff' }}
                                                    onClick={e => setCurPage(curPage + 1)}
                                                >
                                                    Next
                                                </button>
                                            </li>
                                        </ul>
                                    </nav>
                                </>
                            ) : (
                                <div className={cx("data-not-found")}>
                                    <span>
                                        <img
                                            src="https://frontend.tikicdn.com/_desktop-next/static/img/account/empty-order.png"
                                            alt=""
                                        />
                                    </span>
                                    <span className={cx("text")}>
                                        No data found for this period of time!
                                    </span>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default History;
