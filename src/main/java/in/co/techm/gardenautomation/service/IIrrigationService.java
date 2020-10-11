package in.co.techm.gardenautomation.service;

import in.co.techm.gardenautomation.dto.DripName;
import in.co.techm.gardenautomation.dto.SprinklerName;
import in.co.techm.gardenautomation.dto.exception.InvalidPinConfiguration;

public interface IIrrigationService {
    void turnOnSprinkler(final SprinklerName sprinklerName);

    void turnOnDrip(final DripName dripName) throws InvalidPinConfiguration;

    void turnOffDrip(final DripName dripName) throws InvalidPinConfiguration;
}
