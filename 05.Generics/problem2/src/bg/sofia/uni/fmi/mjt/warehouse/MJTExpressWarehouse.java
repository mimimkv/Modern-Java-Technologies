package bg.sofia.uni.fmi.mjt.warehouse;

import bg.sofia.uni.fmi.mjt.warehouse.exceptions.CapacityExceededException;
import bg.sofia.uni.fmi.mjt.warehouse.exceptions.ParcelNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MJTExpressWarehouse<L, P> implements DeliveryServiceWarehouse<L, P>{

    private final int capacity;
    private final int retentionPeriod;

    private SortedMap<MJTExpressLabel<L>, P> items;

    /**
     * Creates a new instance of MJTExpressWarehouse with the given characteristics
     *
     * @param capacity        the total number of parcels that the warehouse can store
     * @param retentionPeriod the maximum number of days for which a parcel can stay in the warehouse, counted from the day the parcel
     * was submitted. After that time passes, the parcel can be removed from the warehouse
     */
    public MJTExpressWarehouse(int capacity, int retentionPeriod) {
        this.capacity = capacity;
        this.retentionPeriod = retentionPeriod;
        this.items = new TreeMap<>();
    }

    @Override
    public void submitParcel(L label, P parcel, LocalDateTime submissionDate) throws CapacityExceededException {
        if (label == null || parcel == null || submissionDate == null) {
            throw new IllegalArgumentException("Args cannot be null");
        }

        if (submissionDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Submission date cannot be in the future");
        }

        if (items.size() == capacity) {
            if (items.firstKey().getCreationDate().plusDays(retentionPeriod).isAfter(LocalDateTime.now())) {
                throw new CapacityExceededException("No more space");
            }
            items.remove(items.firstKey());
        }

        items.put(new MJTExpressLabel<>(label, submissionDate), parcel);
    }

    @Override
    public P getParcel(L label) {
        if (label == null) {
            throw new IllegalArgumentException("Args cannot be null");
        }

        return items.get(wrapLabel(label));
    }

    @Override
    public P deliverParcel(L label) throws ParcelNotFoundException {
        if (label == null) {
            throw new IllegalArgumentException("Args cannot be null");
        }

        if (!items.containsKey(wrapLabel(label))) {
            throw new ParcelNotFoundException("Label not found");
        }

        MJTExpressLabel<L> key = wrapLabel(label);
        P parcel = items.get(key);
        if (parcel == null) {
            throw new ParcelNotFoundException("Parcel with this label not found");
        }

        items.remove(key);
        return parcel;
    }

    @Override
    public double getWarehouseSpaceLeft() {
        return (double) 1 - items.size() / (double) capacity;
    }

    @Override
    public Map<L, P> getWarehouseItems() {
        Map<L, P> map = new HashMap<>();
        for (Map.Entry<MJTExpressLabel<L>, P> entry : items.entrySet()) {
            map.put(entry.getKey().getLabel(), entry.getValue());
        }

        return map;
    }

    @Override
    public Map<L, P> deliverParcelsSubmittedBefore(LocalDateTime before) {
        if (before == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (before.isAfter(LocalDateTime.now())) {
            return getWarehouseItems();
        }

//        SortedMap<MJTExpressLabel<L>, P> head = items.headMap(new MJTExpressLabel<>(null, before));
//        return deliverParcels(head);

        Map<L, P> submittedBefore = new HashMap<>();
        for (Map.Entry<MJTExpressLabel<L>, P> entry : items.entrySet()) {
            if (entry.getKey().getCreationDate().isBefore(before)) {
                submittedBefore.put(entry.getKey().getLabel(), entry.getValue());
                items.remove(entry.getKey());
            }
        }

        return submittedBefore;
    }

    @Override
    public Map<L, P> deliverParcelsSubmittedAfter(LocalDateTime after) {
        if (after == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (after.isAfter(LocalDateTime.now())) {
            return Collections.emptyMap();
        }

//        SortedMap<MJTExpressLabel<L>, P> tail = items.tailMap(new MJTExpressLabel<>(null, after));
//        return deliverParcels(tail);

        Map<L, P> submittedAfter = new HashMap<>();
        for (Map.Entry<MJTExpressLabel<L>, P> entry : items.entrySet()) {

            if (entry.getKey().getCreationDate().isAfter(after)) {
                submittedAfter.put(entry.getKey().getLabel(), entry.getValue());
                items.remove(entry.getKey());
            }
        }

        return submittedAfter;
    }

    private MJTExpressLabel<L> wrapLabel(L label) {
        return new MJTExpressLabel<>(label, null);
    }


    private Map<L, P> deliverParcels(SortedMap<MJTExpressLabel<L>, P> map) {
        Map<L, P> newMap = new HashMap<>();
        for (Map.Entry<MJTExpressLabel<L>, P> entry : map.entrySet()) {
            newMap.put(entry.getKey().getLabel(), entry.getValue());
            items.remove(entry.getKey());
        }

        return newMap;
    }
}
