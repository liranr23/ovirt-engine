package org.ovirt.engine.ui.common.widget.uicommon.template;

import java.util.Date;

import org.ovirt.engine.core.common.businessentities.DiskImage;
import org.ovirt.engine.core.common.businessentities.DiskInterface;
import org.ovirt.engine.core.common.businessentities.ImageStatus;
import org.ovirt.engine.core.common.businessentities.VolumeType;
import org.ovirt.engine.core.common.utils.SizeConverter;
import org.ovirt.engine.ui.common.CommonApplicationConstants;
import org.ovirt.engine.ui.common.system.ClientStorage;
import org.ovirt.engine.ui.common.uicommon.model.SearchableTableModelProvider;
import org.ovirt.engine.ui.common.widget.table.column.AbstractDiskSizeColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractEnumColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractFullDateTimeColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractTextColumnWithTooltip;
import org.ovirt.engine.ui.common.widget.uicommon.AbstractModelBoundTableWidget;
import org.ovirt.engine.ui.uicommonweb.models.templates.TemplateDiskListModel;

import com.google.gwt.event.shared.EventBus;

public class TemplateDiskListModelTable<T extends TemplateDiskListModel> extends AbstractModelBoundTableWidget<DiskImage, T> {

    public TemplateDiskListModelTable(
            SearchableTableModelProvider<DiskImage, T> modelProvider,
            EventBus eventBus, ClientStorage clientStorage, CommonApplicationConstants constants) {
        super(modelProvider, eventBus, clientStorage, false);
    }

    @Override
    public void initTable(CommonApplicationConstants constants) {
        getTable().enableColumnResizing();

        AbstractTextColumnWithTooltip<DiskImage> aliasColumn = new AbstractTextColumnWithTooltip<DiskImage>() {
            @Override
            public String getValue(DiskImage object) {
                return object.getDiskAlias();
            }
        };
        getTable().addColumn(aliasColumn, constants.aliasDisk(), "150px"); //$NON-NLS-1$

        AbstractTextColumnWithTooltip<DiskImage> provisionedSizeColumn = new AbstractTextColumnWithTooltip<DiskImage>() {
            @Override
            public String getValue(DiskImage object) {
                return String.valueOf(object.getSizeInGigabytes()) + " GB"; //$NON-NLS-1$
            }
        };
        getTable().addColumn(provisionedSizeColumn, constants.provisionedSizeDisk(), "150px"); //$NON-NLS-1$

        AbstractDiskSizeColumn<DiskImage> actualSizeColumn = new AbstractDiskSizeColumn<DiskImage>(SizeConverter.SizeUnit.GB) {
            @Override
            protected Long getRawValue(DiskImage object) {
                       return Math.round((object.getActualDiskWithSnapshotsSize()));
            }
        };

        getTable().addColumn(actualSizeColumn, constants.sizeDisk(), "150px"); //$NON-NLS-1$

        AbstractTextColumnWithTooltip<DiskImage> statusColumn = new AbstractEnumColumn<DiskImage, ImageStatus>() {
            @Override
            protected ImageStatus getRawValue(DiskImage object) {
                return object.getImageStatus();
            }
        };

        getTable().addColumn(statusColumn, constants.statusDisk(), "150px"); //$NON-NLS-1$

        AbstractTextColumnWithTooltip<DiskImage> allocationColumn = new AbstractEnumColumn<DiskImage, VolumeType>() {
            @Override
            protected VolumeType getRawValue(DiskImage object) {
                return VolumeType.forValue(object.getVolumeType().getValue());
            }
        };
        getTable().addColumn(allocationColumn, constants.allocationDisk(), "150px"); //$NON-NLS-1$

        AbstractTextColumnWithTooltip<DiskImage> interfaceColumn = new AbstractEnumColumn<DiskImage, DiskInterface>() {
            @Override
            protected DiskInterface getRawValue(DiskImage object) {
                return object.getDiskInterface();
            }
        };
        getTable().addColumn(interfaceColumn, constants.interfaceDisk(), "150px"); //$NON-NLS-1$

        AbstractTextColumnWithTooltip<DiskImage> dateCreatedColumn = new AbstractFullDateTimeColumn<DiskImage>() {
            @Override
            protected Date getRawValue(DiskImage object) {
                return object.getCreationDate();
            }
        };

        getTable().addColumn(dateCreatedColumn, constants.creationDateDisk(), "150px"); //$NON-NLS-1$

        AbstractTextColumnWithTooltip<DiskImage> descriptionColumn = new AbstractTextColumnWithTooltip<DiskImage>() {
            @Override
            public String getValue(DiskImage object) {
                return object.getDescription();
            }
        };

        getTable().addColumn(descriptionColumn, constants.descriptionDisk(), "150px"); //$NON-NLS-1$
    }

}
