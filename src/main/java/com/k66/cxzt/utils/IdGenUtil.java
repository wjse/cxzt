package com.k66.cxzt.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class IdGenUtil {
	private long workerId;
	private long dataCenterId;
	private long sequence = 0L;
	private long workerIdBits = 5L;
	private long dataCenterIdBits = 5L;
	private long maxWorkerId = -1L ^ (-1L << workerIdBits);
	private long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);
	private long sequenceBits = 12L;
	private long workerIdShift = sequenceBits;
	private long dataCenterIdShift = sequenceBits + workerIdBits;
	private long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
	private long sequenceMask = -1L ^ (-1L << sequenceBits);
	private long lastTimestamp = -1L;

	public IdGenUtil(){
		this(new BigDecimal(Math.random()).longValue() , new BigDecimal(Math.random()).longValue());
	}

	public IdGenUtil(long workerId , long dataCenterId){
		if(workerId > maxWorkerId || workerId < 0){
			throw new IllegalArgumentException(String.format("worker id can't be greater than %d or less than 0" , maxWorkerId));
		}

		if(dataCenterId > maxDataCenterId || dataCenterId < 0){
			throw new IllegalArgumentException(String.format("data center id can't be greater than %d or less than 0" , maxDataCenterId));
		}
	}

	public synchronized long nextId(){
		long timestamp = timeGen();
		if(timestamp < lastTimestamp){
			log.error("Clock moved backwards. Refusing to generate id for {} millis" , lastTimestamp - timestamp);
			return 0L;
		}

		if(lastTimestamp == timestamp){
			sequence = (sequence + 1) & sequenceMask;
			if(sequence == 0){
				timestamp = tilNextMillis(lastTimestamp);
			}
		}else{
			sequence = 0L;
		}
		lastTimestamp = timestamp;
		return (timestamp << timestampLeftShift) | (dataCenterId << dataCenterIdShift) | (workerId << workerIdShift) | sequence;
	}

	private long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while(timestamp <= lastTimestamp){
			timestamp = timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}
}
