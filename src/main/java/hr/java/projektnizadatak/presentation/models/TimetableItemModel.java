package hr.java.projektnizadatak.presentation.models;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.shared.Util;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class TimetableItemModel {
	private static final double BEGINNING_TIME = Util.toHours(LocalTime.of(7, 0));
	private static final double ENDING_TIME = Util.toHours(LocalTime.of(22, 0));
	private static final double TIME_SPAN = ENDING_TIME - BEGINNING_TIME;

	private final ScheduleItem scheduleItem;
	private final LocalTime start;
	private final LocalTime end;
	private final double relativeStart;
	private final double relativeEnd;
	private int column;
	private int columnSpan;

	public TimetableItemModel(ScheduleItem item) {
		this.scheduleItem = item;
		this.start = item.start().toLocalTime();
		this.end = item.end().toLocalTime();

		this.relativeStart = (Util.toHours(item.start().toLocalTime()) - BEGINNING_TIME) / TIME_SPAN;
		this.relativeEnd = (ENDING_TIME - Util.toHours(item.end().toLocalTime())) / TIME_SPAN;
		this.column = 0;
		this.columnSpan = 1;
	}

	public static List<TimetableItemModel> organizeItems(List<ScheduleItem> items) {
		if (items.size() == 0) {
			return Collections.emptyList();
		}
		
		var separated = items.stream()
			.map(TimetableItemModel::new)
			.sorted(Comparator
				.comparing(TimetableItemModel::start)
				.thenComparing(TimetableItemModel::end)
			)
			.toList();
		
		// separate into columns
		boolean overlaps;

		do {
			overlaps = false;

			for (int i = 0; i < separated.size() - 1; i++) {
				var m1 = separated.get(i);
				for (int j = i + 1; j < separated.size(); j++) {
					var m2 = separated.get(j);

					if (m1.getColumn() == m2.getColumn() && timespanIntersects(m1, m2)) {
						separated.get(j).setColumn( separated.get(j).getColumn() + 1);
						overlaps = true;
					}
				}
			}

		} while (overlaps);

		// spread out
		int maxColumn = separated.stream()
			.max(Comparator.comparing(m -> m.column))
			.orElseThrow(() -> new RuntimeException(""))
			.getColumn();

		for (int i = 0; i < separated.size(); i++) {
			var m1 = separated.get(i);
			boolean isUnobstructed = true;
			
			for (int j = 0; j < separated.size(); j++) {
				var m2 = separated.get(j);

				if (i != j && m1.getColumn() < m2.getColumn() && timespanIntersects(m1, m2)) {
					isUnobstructed = false;
					break;
				}
			}

			if (isUnobstructed) {
				separated.get(i).setColumnSpan(maxColumn - separated.get(i).getColumn() + 1);
			}
		}

		return separated;
	}

	private static boolean timespanIntersects(TimetableItemModel m1, TimetableItemModel m2) {
		return Util.isBetween(m1.start(), m2.start(), m2.end())
			|| Util.isBetween(m2.start(), m1.start(), m1.end());
	}

	public ScheduleItem getScheduleItem() {return scheduleItem;}

	public LocalTime start() {
		return start;
	}

	public LocalTime end() {
		return end;
	}

	public double getRelativeStart() {return relativeStart;}

	public double getRelativeEnd() {return relativeEnd;}

	public int getColumn() {return column;}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getColumnSpan() {return columnSpan;}

	public void setColumnSpan(int columnSpan) {
		this.columnSpan = columnSpan;
	}
}
