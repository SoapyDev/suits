export enum Priority {
  VERY_LOW = 1,
  LOW = 2,
  NORMAL = 3,
  HIGH = 4,
  VERY_HIGH = 5,
}


export function priorityToString(priority: Priority) {
  switch (priority) {
    case Priority.VERY_LOW: return "Très basse";
    case Priority.LOW: return "Basse";
    case Priority.NORMAL: return "Normal";
    case Priority.HIGH: return "Haute";
    case Priority.VERY_HIGH: return "Très haute";
  }
}

