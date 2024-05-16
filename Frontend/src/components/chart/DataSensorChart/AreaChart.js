import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import styles from './Area.module.scss';

const cx = classNames.bind(styles);

function AreaChartComponent(props) {
  const [data, setData] = useState([]);

  useEffect(() => {
    const dataSensor = props.data;
    if (dataSensor) {
      const newData = [...data];
      const date = new Date(dataSensor.timestamp);

      const hour = date.getHours();
      const minutes = date.getMinutes();
      const seconds = date.getSeconds();
      newData.push({
        name: `${hour}:${minutes}:${seconds}`,
        Temp: dataSensor.temperature,
        Humidity: dataSensor.humidity,
      });

      if (newData.length > 8) {
        newData.shift();
      }

      setData(newData);
    }
  }, [props.data]);

  return (
    <div>
      <div className={cx('container_chart')}>
        <ResponsiveContainer width="100%" height={360}>
          <AreaChart data={data}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" />
            <YAxis domain={['auto', 'auto']} label={{ value: '°C', position: 'insideLeft' }}/>
            <Tooltip />
            <CartesianGrid strokeDasharray="3 3" />
            <Area type="monotone" dataKey="Temp" stackId="1" stroke="#8884d8" fill="#8884d8" />
            {/* <Area type="monotone" dataKey="Humidity" stackId="1" stroke="#82ca9d" fill="#82ca9d" /> */}
          </AreaChart>
        </ResponsiveContainer>
        <h6>Biểu đồ  nhiệt độ </h6>
      </div>
      <div className={cx('container_chart')} style={{margin: '5px 0'}}>
        <ResponsiveContainer width="100%" height={360}>
          <AreaChart data={data}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" />

            <YAxis domain={['auto', 'auto']} label={{ value: '%',  position: 'insideLeft' }}/>

            <Tooltip />
            <CartesianGrid strokeDasharray="3 3" />
            {/* <Area type="monotone" dataKey="Temp" stackId="1" stroke="#8884d8" fill="#8884d8" /> */}
            <Area type="monotone" dataKey="Humidity" stackId="1" stroke="#82ca9d" fill="#82ca9d" />
          </AreaChart>
        </ResponsiveContainer>
        <h6>Biểu đồ  độ ẩm</h6>
      </div>
    </div>
  );
}

export default AreaChartComponent;
