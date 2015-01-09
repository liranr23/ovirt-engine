package org.ovirt.engine.ui.webadmin.section.main.view.tab;

import java.util.List;

import org.ovirt.engine.core.common.businessentities.StorageDomain;
import org.ovirt.engine.core.common.businessentities.StorageDomainType;
import org.ovirt.engine.core.common.businessentities.StorageFormatType;
import org.ovirt.engine.core.common.businessentities.StorageType;
import org.ovirt.engine.core.searchbackend.StorageDomainFieldAutoCompleter;
import org.ovirt.engine.ui.common.idhandler.ElementIdHandler;
import org.ovirt.engine.ui.common.uicommon.model.MainModelProvider;
import org.ovirt.engine.ui.common.widget.action.ActionButtonDefinition;
import org.ovirt.engine.ui.common.widget.action.CommandLocation;
import org.ovirt.engine.ui.common.widget.table.column.AbstractEnumColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractStorageSizeColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractTextColumnWithTooltip;
import org.ovirt.engine.ui.uicommonweb.ReportInit;
import org.ovirt.engine.ui.uicommonweb.UICommand;
import org.ovirt.engine.ui.uicommonweb.models.storage.StorageListModel;
import org.ovirt.engine.ui.uicompat.EnumTranslator;
import org.ovirt.engine.ui.uicompat.Event;
import org.ovirt.engine.ui.uicompat.EventArgs;
import org.ovirt.engine.ui.uicompat.IEventListener;
import org.ovirt.engine.ui.webadmin.ApplicationConstants;
import org.ovirt.engine.ui.webadmin.section.main.presenter.tab.MainTabStoragePresenter;
import org.ovirt.engine.ui.webadmin.section.main.view.AbstractMainTabWithDetailsTableView;
import org.ovirt.engine.ui.webadmin.uicommon.ReportActionsHelper;
import org.ovirt.engine.ui.webadmin.widget.action.WebAdminButtonDefinition;
import org.ovirt.engine.ui.webadmin.widget.action.WebAdminMenuBarButtonDefinition;
import org.ovirt.engine.ui.webadmin.widget.table.column.CommentColumn;
import org.ovirt.engine.ui.webadmin.widget.table.column.StorageDomainSharedStatusColumn;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

public class MainTabStorageView extends AbstractMainTabWithDetailsTableView<StorageDomain, StorageListModel> implements MainTabStoragePresenter.ViewDef {

    interface ViewIdHandler extends ElementIdHandler<MainTabStorageView> {
        ViewIdHandler idHandler = GWT.create(ViewIdHandler.class);
    }

    @Inject
    public MainTabStorageView(MainModelProvider<StorageDomain, StorageListModel> modelProvider,
            ApplicationConstants constants) {
        super(modelProvider);
        ViewIdHandler.idHandler.generateAndSetIds(this);
        initTable(constants);
        initWidget(getTable());
    }

    void initTable(final ApplicationConstants constants) {
        getTable().enableColumnResizing();

        getTable().addColumn(new StorageDomainSharedStatusColumn(), constants.empty(), "30px"); //$NON-NLS-1$

        AbstractTextColumnWithTooltip<StorageDomain> nameColumn = new AbstractTextColumnWithTooltip<StorageDomain>() {
            @Override
            public String getValue(StorageDomain object) {
                return object.getStorageName();
            }
        };
        nameColumn.makeSortable(StorageDomainFieldAutoCompleter.NAME);
        getTable().addColumn(nameColumn, constants.domainNameStorage(), "150px"); //$NON-NLS-1$

        CommentColumn<StorageDomain> commentColumn = new CommentColumn<StorageDomain>();
        getTable().addColumnWithHtmlHeader(commentColumn, commentColumn.getHeaderHtml(), "30px"); //$NON-NLS-1$

        AbstractTextColumnWithTooltip<StorageDomain> domainTypeColumn = new AbstractEnumColumn<StorageDomain, StorageDomainType>() {
            @Override
            protected StorageDomainType getRawValue(StorageDomain object) {
                return object.getStorageDomainType();
            }
        };
        getTable().addColumn(domainTypeColumn, constants.domainTypeStorage(), "150px"); //$NON-NLS-1$

        AbstractTextColumnWithTooltip<StorageDomain> storageTypeColumn = new AbstractEnumColumn<StorageDomain, StorageType>() {
            @Override
            protected StorageType getRawValue(StorageDomain object) {
                return object.getStorageType();
            }
        };
        storageTypeColumn.makeSortable(StorageDomainFieldAutoCompleter.TYPE);
        getTable().addColumn(storageTypeColumn, constants.storageTypeStorage(), "150px"); //$NON-NLS-1$

        AbstractTextColumnWithTooltip<StorageDomain> formatColumn = new AbstractEnumColumn<StorageDomain, StorageFormatType>() {
            @Override
            protected StorageFormatType getRawValue(StorageDomain object) {
                return object.getStorageFormat();
            }
        };
        getTable().addColumn(formatColumn, constants.formatStorage(), "140px"); //$NON-NLS-1$

        AbstractTextColumnWithTooltip<StorageDomain> crossDataCenterStatusColumn =
                new AbstractTextColumnWithTooltip<StorageDomain>() {
                    @Override
                    public String getValue(StorageDomain object) {
                        if (object.getStorageDomainType() == StorageDomainType.ISO) {
                            return EnumTranslator.getInstance().get(object.getStorageDomainSharedStatus());
                        } else {
                            return EnumTranslator.getInstance().get(object.getStatus());
                        }
                    }
                };
        crossDataCenterStatusColumn.makeSortable(StorageDomainFieldAutoCompleter.STATUS);
        getTable().addColumn(crossDataCenterStatusColumn, constants.crossDcStatusStorage(), "210px"); //$NON-NLS-1$

        AbstractStorageSizeColumn<StorageDomain> totalSpaceColumn = new AbstractStorageSizeColumn<StorageDomain>() {
            @Override
            public Long getRawValue(StorageDomain object) {
                long totalSpace = object.getTotalDiskSize() != null ? object.getTotalDiskSize() : 0;
                return (long) totalSpace;
            }
        };
        getTable().addColumn(totalSpaceColumn, constants.totalSpaceStorage(), "130px"); //$NON-NLS-1$

        AbstractStorageSizeColumn<StorageDomain> freeSpaceColumn = new AbstractStorageSizeColumn<StorageDomain>() {
            @Override
            public Long getRawValue(StorageDomain object) {
                long availableDiskSize = object.getAvailableDiskSize() != null ? object.getAvailableDiskSize() : 0;
                return (long) availableDiskSize;
            }
        };
        freeSpaceColumn.makeSortable(StorageDomainFieldAutoCompleter.SIZE);
        getTable().addColumn(freeSpaceColumn, constants.freeSpaceStorage(), "130px"); //$NON-NLS-1$

        AbstractTextColumnWithTooltip<StorageDomain> descriptionColumn = new AbstractTextColumnWithTooltip<StorageDomain>() {
            @Override
            public String getValue(StorageDomain object) {
                return object.getDescription();
            }
        };
        descriptionColumn.makeSortable(StorageDomainFieldAutoCompleter.DESCRIPTION);
        getTable().addColumn(descriptionColumn, constants.domainDescriptionStorage(), "200px"); //$NON-NLS-1$

        getTable().addActionButton(new WebAdminButtonDefinition<StorageDomain>(constants.newDomainStorage()) {
            @Override
            protected UICommand resolveCommand() {
                return getMainModel().getNewDomainCommand();
            }
        });
        getTable().addActionButton(new WebAdminButtonDefinition<StorageDomain>(constants.importDomainStorage()) {
            @Override
            protected UICommand resolveCommand() {
                return getMainModel().getImportDomainCommand();
            }
        });
        getTable().addActionButton(new WebAdminButtonDefinition<StorageDomain>(constants.editStorage()) {
            @Override
            protected UICommand resolveCommand() {
                return getMainModel().getEditCommand();
            }
        });
        getTable().addActionButton(new WebAdminButtonDefinition<StorageDomain>(constants.removeStorage()) {
            @Override
            protected UICommand resolveCommand() {
                return getMainModel().getRemoveCommand();
            }
        });
        getTable().addActionButton(new WebAdminButtonDefinition<StorageDomain>(constants.destroyStorage(),
                CommandLocation.OnlyFromContext) {
            @Override
            protected UICommand resolveCommand() {
                return getMainModel().getDestroyCommand();
            }
        });
        if (ReportInit.getInstance().isReportsEnabled()) {
            updateReportsAvailability(constants);
        } else {
            getMainModel().getReportsAvailabilityEvent().addListener(new IEventListener<EventArgs>() {
                @Override
                public void eventRaised(Event<? extends EventArgs> ev, Object sender, EventArgs args) {
                    updateReportsAvailability(constants);
                }
            });
        }
    }

    private void updateReportsAvailability(ApplicationConstants constants) {
        if (ReportInit.getInstance().isReportsEnabled()) {
            List<ActionButtonDefinition<StorageDomain>> resourceSubActions =
                    ReportActionsHelper.getInstance().getResourceSubActions("Storage", getModelProvider()); //$NON-NLS-1$
            if (resourceSubActions != null && resourceSubActions.size() > 0) {
                getTable().addActionButton(new WebAdminMenuBarButtonDefinition<StorageDomain>(constants.showReportStorage(),
                        resourceSubActions));
            }
        }
    }

}
