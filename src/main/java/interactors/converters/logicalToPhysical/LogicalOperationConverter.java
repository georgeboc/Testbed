package interactors.converters.logicalToPhysical;

import entities.exceptions.ColumnNotFoundException;
import entities.operations.physical.PhysicalOperation;
import entities.profiles.ProfileEstimation;

public interface LogicalOperationConverter {
    PhysicalOperation convert(ProfileEstimation profileEstimation) throws ColumnNotFoundException;
}
