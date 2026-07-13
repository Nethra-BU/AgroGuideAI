import java.io.*;
import java.util.*;

public class AgroGuideAI {

    static Scanner sc = new Scanner(System.in);

    static class Crop {

    	String soilType;
    	String season;
    	String water;
    	String cropName;
    	String fertilizer;
    	String duration;

    	int confidence;

    	double yieldPerAcre;

	int temperature;
	int humidity;
	int rainfall;

	String marketDemand;
	double pricePerTon;

    	Crop(String soilType,
         	String season,
         	String water,
         	String cropName,
         	String fertilizer,
         	String duration,
         	int confidence,
         	double yieldPerAcre,
         	int temperature,
         	int humidity,
         	int rainfall) {

        	this.soilType = soilType;
        	this.season = season;
        	this.water = water;
        	this.cropName = cropName;
        	this.fertilizer = fertilizer;
        	this.duration = duration;
        	this.confidence = confidence;
        	this.yieldPerAcre = yieldPerAcre;

        	this.temperature = temperature;
        	this.humidity = humidity;
        	this.rainfall = rainfall;
		this.marketDemand = marketDemand;
    		this.pricePerTon = pricePerTon;
    	}

}

    static ArrayList<Crop> crops = new ArrayList<>();

    public static void main(String[] args) {

        loadCropData();

        while (true) {

            System.out.println("\n=================================");
            System.out.println("   AGROGUIDE AI");
            System.out.println("=================================");
            System.out.println("1. Crop Recommendation");
            System.out.println("2. Crop Information");
            System.out.println("3. Crop Rotation Advice");
            System.out.println("4. View Recommendation History");
            System.out.println("5. Exit");

            System.out.print("Enter Choice: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    recommendCrop();
                    break;

                case 2:
                    cropInformation();
                    break;

                case 3:
                    cropRotation();
                    break;

                case 4:
                    viewHistory();
                    break;

                case 5:
                    System.out.println("Thank You!");
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    static void loadCropData() {

        try {

            BufferedReader br =
                    new BufferedReader(
                            new FileReader("crop_data1.csv"));

            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                Crop crop = new Crop(
        		data[0],
        		data[1],
        		data[2],
        		data[3],
        		data[4],
        		data[5],
        		Integer.parseInt(data[6]),
        		Double.parseDouble(data[7]),
        		Integer.parseInt(data[8]),
        		Integer.parseInt(data[9]),
        		Integer.parseInt(data[10])
		);

                crops.add(crop);

		System.out.println(
    	crop.soilType + " | " +
    	crop.season + " | " +
    	crop.water + " | " +
    	crop.cropName
	);
            }

            br.close();

        } catch (Exception e) {

            System.out.println(
                    "Error loading crop data: "
                            + e.getMessage());
        }
    }

    static void recommendCrop() {

        System.out.print("Enter Soil Type: ");
        String soil = sc.nextLine();

        System.out.print("Enter Season: ");
        String season = sc.nextLine();

        System.out.print("Enter Water Availability: ");
        String water = sc.nextLine();

        System.out.print("Enter Land Area (Acres): ");
        double area =
                Double.parseDouble(sc.nextLine());

        ArrayList<Crop> matches =
                new ArrayList<>();

        for (Crop crop : crops) {

            if (crop.soilType.equalsIgnoreCase(soil)
                    &&
                    crop.season.equalsIgnoreCase(season)
                    &&
                    crop.water.equalsIgnoreCase(water)) {

                matches.add(crop);
            }
        }

        if (matches.isEmpty()) {

            System.out.println(
                    "\nNo exact match found.");

            return;
        }

        matches.sort((a, b) ->
                b.confidence - a.confidence);

        System.out.println(
                "\n===== TOP RECOMMENDATIONS =====");

        int count =
                Math.min(3, matches.size());

        for (int i = 0; i < count; i++) {

            Crop c = matches.get(i);

            double estimatedYield =
                    c.yieldPerAcre * area;

            System.out.println(
                    "\nRecommendation #" + (i + 1));

            System.out.println(
                    "Crop: " + c.cropName);

            System.out.println(
                    "Confidence: "
                            + c.confidence + "%");

            System.out.println(
                    "Fertilizer: "
                            + c.fertilizer);

            System.out.println(
                    "Duration: "
                            + c.duration);

            System.out.println(
                    "Estimated Yield: "
                            + estimatedYield
                            + " Tons");
        }

        saveHistory(
                soil,
                season,
                water,
                matches.get(0).cropName
        );
    }

    static void cropInformation() {

        System.out.print(
                "Enter Crop Name: ");

        String cropName =
                sc.nextLine();

        for (Crop crop : crops) {

            if (crop.cropName.equalsIgnoreCase(
                    cropName)) {

                System.out.println(
                        "\nCrop: "
                                + crop.cropName);

                System.out.println(
                        "Soil Type: "
                                + crop.soilType);

                System.out.println(
                        "Season: "
                                + crop.season);

                System.out.println(
                        "Water: "
                                + crop.water);

                System.out.println(
                        "Fertilizer: "
                                + crop.fertilizer);

                System.out.println(
                        "Duration: "
                                + crop.duration);

                return;
            }
        }

        System.out.println(
                "Crop not found.");
    }

    static void cropRotation() {

        try {

            System.out.print(
                    "Enter Previous Crop: ");

            String crop =
                    sc.nextLine();

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(
                                    "crop_rotation.csv"));

            br.readLine();

            String line;

            while ((line = br.readLine())
                    != null) {

                String[] data =
                        line.split(",");

                if (data[0].equalsIgnoreCase(
                        crop)) {

                    System.out.println(
                            "\nSuggested Next Crop: "
                                    + data[1]);

                    System.out.println(
                            "Reason: "
                                    + data[2]);

                    br.close();

                    return;
                }
            }

            br.close();

            System.out.println(
                    "No crop rotation advice found.");

        } catch (Exception e) {

            System.out.println(
                    e.getMessage());
        }
    }

    static void saveHistory(
            String soil,
            String season,
            String water,
            String crop) {

        try {

            FileWriter fw =
                    new FileWriter(
                            "history.txt",
                            true);

            fw.write(
                    soil + ","
                            + season + ","
                            + water + ","
                            + crop + "\n");

            fw.close();

        } catch (Exception e) {

            System.out.println(
                    e.getMessage());
        }
    }

    static void viewHistory() {

        try {

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(
                                    "history.txt"));

            String line;

            System.out.println(
                    "\n===== HISTORY =====");

            while ((line =
                    br.readLine()) != null) {

                System.out.println(
                        line);
            }

            br.close();

        } catch (Exception e) {

            System.out.println(
                    "No history available.");
        }
    }
}