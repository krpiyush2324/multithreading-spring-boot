package com.example.multithreading.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.multithreading.model.Car;
import com.example.multithreading.service.CarService;

@RestController
@RequestMapping("/car")
public class CarController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);

	@Autowired
	private CarService carService;

	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity uploadFile(@RequestParam MultipartFile[] files) {
		try {
			for (final MultipartFile file : files) {
				carService.saveCar(file.getInputStream());
			}
			return ResponseEntity.status(HttpStatus.CREATED).body("added into db");
		} catch (final Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

//	@RequestMapping(value= "/get",method = RequestMethod.GET, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
//			MediaType.APPLICATION_JSON_VALUE })
	@GetMapping("/get")
	public @ResponseBody ResponseEntity getAllCars() {
		// return
		// carService.getAllCars().<ResponseEntity>thenApply(ResponseEntity::ok).exceptionally(handleGetCarFailure);
		try {
			CompletableFuture<List<Car>> cars1 = carService.getAllCars();
			CompletableFuture<List<Car>> cars2 = carService.getAllCars();
			CompletableFuture<List<Car>> cars3 = carService.getAllCars();

			Void join = CompletableFuture.allOf(cars1, cars2, cars3).join();
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (final Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	private static Function<Throwable, ResponseEntity<? extends List<Car>>> handleGetCarFailure = throwable -> {
		LOGGER.error("Failed to read records: {}", throwable);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	};

}
