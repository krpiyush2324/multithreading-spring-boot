package com.example.multithreading.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.multithreading.model.Car;
import com.example.multithreading.repository.CarRepository;

@Service
public class CarService {

	private static final org.jboss.logging.Logger LOGGER = LoggerFactory.logger(CarService.class);

	@Autowired
	private CarRepository carRepository;

	@Async
	public CompletableFuture<List<Car>> saveCar(final InputStream inputStream) throws Exception {

		final long start = System.currentTimeMillis();
		List<Car> cars = parseCSVFile(inputStream);
		LOGGER.info("Saving a list of cars of size {} records", cars.size(), null);
		cars = carRepository.saveAll(cars);
		System.out.println("cars= " + cars.size());
		LOGGER.info("Elapsed time: {}", (System.currentTimeMillis() - start), null);
		return CompletableFuture.completedFuture(cars);

	}

	@Async
	public CompletableFuture<List<Car>> getAllCars() {

		LOGGER.info("Request to get a list of cars");
		System.out.println("Thread Name= " + Thread.currentThread().getName());
		final List<Car> cars = carRepository.findAll();
		return CompletableFuture.completedFuture(cars);
	}

	private List<Car> parseCSVFile(InputStream inputStream) throws Exception {

		List<Car> cars = new ArrayList<Car>();
		try {

			try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
				String line;
				while ((line = br.readLine()) != null) {
					final String[] data = line.split(";");
					final Car car = new Car();
					car.setManufacturer(data[0]);
					car.setModel(data[1]);
					car.setType(data[2]);
					cars.add(car);
				}
				return cars;
			}

		} catch (final IOException e) {
			LOGGER.error("Failed to parse CSV file {}", e);
			throw new Exception("Failed to parse CSV file {}", e);
		}
	}

}
