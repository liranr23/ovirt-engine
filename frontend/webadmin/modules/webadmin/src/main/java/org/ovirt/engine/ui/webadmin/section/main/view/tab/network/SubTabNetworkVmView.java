package org.ovirt.engine.ui.webadmin.section.main.view.tab.network;

import java.util.Arrays;

import javax.inject.Inject;

import org.ovirt.engine.core.common.businessentities.VM;
import org.ovirt.engine.core.common.businessentities.network.NetworkView;
import org.ovirt.engine.core.common.businessentities.network.VmNetworkInterface;
import org.ovirt.engine.core.common.utils.PairQueryable;
import org.ovirt.engine.ui.common.idhandler.ElementIdHandler;
import org.ovirt.engine.ui.common.uicommon.model.SearchableDetailModelProvider;
import org.ovirt.engine.ui.common.view.ViewRadioGroup;
import org.ovirt.engine.ui.common.widget.table.column.AbstractNullableNumberColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractRxTxRateColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractTextColumnWithTooltip;
import org.ovirt.engine.ui.common.widget.table.column.NicActivateStatusColumn;
import org.ovirt.engine.ui.uicommonweb.UICommand;
import org.ovirt.engine.ui.uicommonweb.models.networks.NetworkListModel;
import org.ovirt.engine.ui.uicommonweb.models.networks.NetworkVmFilter;
import org.ovirt.engine.ui.uicommonweb.models.networks.NetworkVmListModel;
import org.ovirt.engine.ui.webadmin.ApplicationConstants;
import org.ovirt.engine.ui.webadmin.ApplicationTemplates;
import org.ovirt.engine.ui.webadmin.section.main.presenter.tab.network.SubTabNetworkVmPresenter;
import org.ovirt.engine.ui.webadmin.section.main.view.AbstractSubTabTableView;
import org.ovirt.engine.ui.webadmin.widget.action.WebAdminButtonDefinition;
import org.ovirt.engine.ui.webadmin.widget.table.column.VmStatusColumn;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RadioButton;

public class SubTabNetworkVmView extends AbstractSubTabTableView<NetworkView, PairQueryable<VmNetworkInterface, VM>, NetworkListModel, NetworkVmListModel>
        implements SubTabNetworkVmPresenter.ViewDef {

    interface ViewIdHandler extends ElementIdHandler<SubTabNetworkVmView> {
        ViewIdHandler idHandler = GWT.create(ViewIdHandler.class);
    }

    private final ApplicationConstants constants;
    private final ApplicationTemplates templates;
    private final ViewRadioGroup<NetworkVmFilter> viewRadioGroup;

    @Inject
    public SubTabNetworkVmView(SearchableDetailModelProvider<PairQueryable<VmNetworkInterface, VM>, NetworkListModel, NetworkVmListModel> modelProvider, ApplicationConstants constants, ApplicationTemplates templates) {
        super(modelProvider);
        this.constants = constants;
        this.templates = templates;
        viewRadioGroup = new ViewRadioGroup<NetworkVmFilter>(Arrays.asList(NetworkVmFilter.values()));
        viewRadioGroup.setSelectedValue(NetworkVmFilter.running);
        viewRadioGroup.addStyleName("stnvmv_radioGroup_pfly_fix"); //$NON-NLS-1$
        initTable();
        initWidget(getTable());
    }

    @Override
    protected void generateIds() {
        ViewIdHandler.idHandler.generateAndSetIds(this);
    }

    private void initTableOverhead() {
        viewRadioGroup.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (((RadioButton) event.getSource()).getValue()) {
                    handleRadioButtonClick(event);
                }
            }
        });

        getTable().setTableOverhead(viewRadioGroup);
        getTable().setTableTopMargin(20);
    }

    private void handleRadioButtonClick(ClickEvent event) {
        getDetailModel().setViewFilterType((viewRadioGroup.getSelectedValue()));

        boolean running = viewRadioGroup.getSelectedValue() == NetworkVmFilter.running;

        getTable().ensureColumnPresent(vmStatusColumn, constants.empty(), true, "30px"); //$NON-NLS-1$

        getTable().ensureColumnPresent(nameColumn, constants.nameVm(), true, "150px"); //$NON-NLS-1$

        getTable().ensureColumnPresent(clusterColumn, constants.clusterVm(), true, "150px"); //$NON-NLS-1$

        getTable().ensureColumnPresent(ipColumn, constants.ipVm(), true, "150px"); //$NON-NLS-1$

        getTable().ensureColumnPresent(fqdnColumn, constants.fqdn(), true, "150px"); //$NON-NLS-1$

        getTable().ensureColumnPresent(nicActivateStatusColumn, constants.vnicStatusNetworkVM(), true, "100px"); //$NON-NLS-1$

        getTable().ensureColumnPresent(vnicNameColumn, constants.vnicNetworkVM(), true, "100px"); //$NON-NLS-1$

        getTable().ensureColumnPresent(rxColumn,
                templates.sub(constants.rxNetworkVM(), constants.mbps()).asString(),
                running, "100px"); //$NON-NLS-1$

        getTable().ensureColumnPresent(txColumn,
                templates.sub(constants.txNetworkVM(), constants.mbps()).asString(),
                running, "100px"); //$NON-NLS-1$

        getTable().ensureColumnPresent(totalRxColumn,
                templates.sub(constants.rxTotal(), constants.bytes()).asString(),
                running,
                "150px"); //$NON-NLS-1$

        getTable().ensureColumnPresent(totalTxColumn,
                templates.sub(constants.txTotal(), constants.bytes()).asString(),
                running,
                "150px"); //$NON-NLS-1$

        getTable().ensureColumnPresent(descriptionColumn, constants.descriptionVm(), true, "150px"); //$NON-NLS-1$
    }

    private final VmStatusColumn<PairQueryable<VmNetworkInterface, VM>> vmStatusColumn =
            new VmStatusColumn<PairQueryable<VmNetworkInterface, VM>>();
    private final AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>> nameColumn =
            new AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>>() {
                @Override
                public String getValue(PairQueryable<VmNetworkInterface, VM> object) {
                    return object.getSecond().getName();
                }
            };
    private final AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>> clusterColumn =
            new AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>>() {
                @Override
                public String getValue(PairQueryable<VmNetworkInterface, VM> object) {
                    return object.getSecond().getVdsGroupName();
                }
            };
    private final AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>> ipColumn =
            new AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>>() {
                @Override
                public String getValue(PairQueryable<VmNetworkInterface, VM> object) {
                    return object.getSecond().getVmIp();
                }
            };

    private final AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>> fqdnColumn =
            new AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>>() {
                @Override
                public String getValue(PairQueryable<VmNetworkInterface, VM> object) {
                    setTitle(object.getSecond().getVmFQDN());
                    return object.getSecond().getVmFQDN();
                }
            };

    private final NicActivateStatusColumn<PairQueryable<VmNetworkInterface, VM>> nicActivateStatusColumn =
            new NicActivateStatusColumn<PairQueryable<VmNetworkInterface, VM>>();

    private final AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>> vnicNameColumn =
            new AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>>() {
                @Override
                public String getValue(PairQueryable<VmNetworkInterface, VM> object) {
                    return object.getFirst().getName();
                }
            };

    private final AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>> rxColumn =
            new AbstractRxTxRateColumn<PairQueryable<VmNetworkInterface, VM>>() {
                @Override
                protected Double getRate(PairQueryable<VmNetworkInterface, VM> object) {
                    return object.getFirst().getStatistics().getReceiveRate();
                }

                @Override
                protected Double getSpeed(PairQueryable<VmNetworkInterface, VM> object) {
                    if (object.getFirst().getSpeed() != null) {
                        return object.getFirst().getSpeed().doubleValue();
                    } else {
                        return null;
                    }
                }
            };

    private final AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>> txColumn =
            new AbstractRxTxRateColumn<PairQueryable<VmNetworkInterface, VM>>() {
                @Override
                protected Double getRate(PairQueryable<VmNetworkInterface, VM> object) {
                    return object.getFirst().getStatistics().getTransmitRate();
                }

                @Override
                protected Double getSpeed(PairQueryable<VmNetworkInterface, VM> object) {
                    if (object.getFirst().getSpeed() != null) {
                        return object.getFirst().getSpeed().doubleValue();
                    } else {
                        return null;
                    }
                }
            };

    private final AbstractNullableNumberColumn<PairQueryable<VmNetworkInterface, VM>> totalRxColumn =
            new AbstractNullableNumberColumn<PairQueryable<VmNetworkInterface, VM>>() {
        @Override
        protected Number getRawValue(PairQueryable<VmNetworkInterface, VM> object) {
            return object.getFirst() == null ? null : object.getFirst().getStatistics().getReceivedBytes();
        }
    };

    private final AbstractNullableNumberColumn<PairQueryable<VmNetworkInterface, VM>> totalTxColumn =
            new AbstractNullableNumberColumn<PairQueryable<VmNetworkInterface, VM>>() {
        @Override
        protected Number getRawValue(PairQueryable<VmNetworkInterface, VM> object) {
            return object.getFirst() == null ? null : object.getFirst().getStatistics().getTransmittedBytes();
        }
    };

    private final AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>> descriptionColumn =
            new AbstractTextColumnWithTooltip<PairQueryable<VmNetworkInterface, VM>>() {
                @Override
                public String getValue(PairQueryable<VmNetworkInterface, VM> object) {
                    return object.getSecond().getDescription();
                }
            };

    private void initTable() {
        getTable().enableColumnResizing();
        initTableOverhead();
        handleRadioButtonClick(null);
        initSorting();

        getTable().addActionButton(new WebAdminButtonDefinition<PairQueryable<VmNetworkInterface, VM>>(constants.removeInterface()) {
            @Override
            protected UICommand resolveCommand() {
                return getDetailModel().getRemoveCommand();
            }
        });

    }

    private void initSorting() {
        vmStatusColumn.makeSortable();
        nameColumn.makeSortable();
        clusterColumn.makeSortable();
        ipColumn.makeSortable();
        fqdnColumn.makeSortable();
        nicActivateStatusColumn.makeSortable();
        vnicNameColumn.makeSortable();
        rxColumn.makeSortable();
        txColumn.makeSortable();
        totalRxColumn.makeSortable();
        totalTxColumn.makeSortable();
        descriptionColumn.makeSortable();
    }

}

