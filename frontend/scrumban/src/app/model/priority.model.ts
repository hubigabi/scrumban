export class Priority {
  name: string;
  value: number;

  constructor(name: string, value: number) {
    this.name = name;
    this.value = value;
  }
}

export const PRIORITY_LOW = new Priority('Low', 0);
export const PRIORITY_MEDIUM = new Priority('Medium', 1);
export const PRIORITY_HIGH = new Priority('High', 2);
export const PRIORITY_VERY_HIGH = new Priority('Very high', 3);

export const ALL_PRIORITY: Priority[] = [
  PRIORITY_LOW, PRIORITY_MEDIUM, PRIORITY_HIGH, PRIORITY_VERY_HIGH
];
